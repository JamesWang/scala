package com.aidokay.music

import akka.actor.typed.scaladsl.adapter.{ClassicActorContextOps, ClassicActorSystemOps}
import akka.actor.typed.{ActorRef => TypedActorRef}
import akka.actor.{Actor, ActorLogging, ActorRef, Props, ActorSystem => ClassicAS}
import akka.io.Tcp.{Received, Write}
import akka.util.ByteString
import com.aidokay.music.JokeBox._
import com.typesafe.config.{Config, ConfigFactory}

import java.net.InetSocketAddress

object NetController extends App {
  class Controller(
      connection: ActorRef,
      remote: InetSocketAddress
  ) extends Actor
      with ActorLogging {

    context.watch(connection)

    type CmdCreator = ActorRef => MusicBox with Product

    val cmdMapping: Map[String, CmdCreator] = Map(
      "/list" -> ListMusic,
      "/play" -> PlayMusic,
      "/pause" -> PauseMusic
    )
    protected def tracks(str: String): List[String] =
      str.substring(10).split(",").toList

    protected def createCommand(strCmd: String): MusicBox = {
      cmdMapping.get(strCmd.trim) match {
        case None if strCmd.startsWith("/schedule") =>
          val trks = tracks(strCmd)
          println(s"Schedule[$trks] command received")
          ScheduleMusic(trks, context.self)
        case Some(cmd) =>
          println(s"[$cmd] command received")
          cmd.apply(context.self)
        case None =>
          println(s"Invalid command $strCmd")
          Ignore
          //throw new RuntimeException("Invalid Command")
      }
    }

    override def receive: Receive = { case Received(command) =>
      val cmd = createCommand(command.utf8String.trim)
      cmd match {
        case ListMusic(_) => jokeBoxHandler ! cmd
          context.become {
            case ListedMusics(musics) =>
              println(s"listed musics: $musics")
              connection ! Write(ByteString.fromString(musics.mkString("\n")))
              context.unbecome()
          }
        case _ => jokeBoxHandler ! cmd
      }

    }
  }
  val config: Config = ConfigFactory.parseString("akka.loglevel = DEBUG")
  implicit val system: ClassicAS = ClassicAS("MusicNetController", config)

  system.actorOf(
    Props(classOf[MusicManager], classOf[Controller]),
    "netController"
  )
  import com.aidokay.music.tracks.AudioProviders.audioProvider
  val jokeBoxHandler = system.spawn(new JokeBoxHandler(audioProvider).apply(), "jokeBoxHandler")

}
