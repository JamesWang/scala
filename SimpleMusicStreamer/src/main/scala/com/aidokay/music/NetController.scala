package com.aidokay.music

import akka.actor.{
  Actor,
  ActorLogging,
  ActorRef,
  Props,
  ActorSystem => ClassicAS
}
import akka.io.Tcp.Received
import com.typesafe.config.{Config, ConfigFactory}
import akka.actor.typed.{ActorRef => TypedActorRef}
import com.aidokay.music.Commands.{
  Command,
  ListMusic,
  Pause,
  PlayMusic,
  Schedule
}

import java.net.InetSocketAddress

object NetController extends App {
  class Controller(
      connection: ActorRef,
      remote: InetSocketAddress,
      jokeBox: TypedActorRef[Command]
  ) extends Actor
      with ActorLogging {
    context.watch(connection)

    override def receive: Receive = { case Received(command) =>
      command.utf8String.trim match {
        case "/list" =>
          println("Received: /list")
          jokeBox ! ListMusic
        case "/play" =>
          println("Received /play")
          jokeBox ! PlayMusic
        case "/pause" =>
          println("Received: /pause")
          jokeBox ! Pause
        case x if x.startsWith("/schedule") =>
          val tracks = x.substring(10).split(",").toList
          println(s"Schedule[$tracks] command received")
          jokeBox ! Schedule(tracks)
      }
    }
  }
  val config: Config = ConfigFactory.parseString("akka.loglevel = DEBUG")
  implicit val system: ClassicAS = ClassicAS("MusicNetController", config)

  system.actorOf(
    Props(classOf[MusicManager], classOf[Controller]),
    "netController"
  )

}
