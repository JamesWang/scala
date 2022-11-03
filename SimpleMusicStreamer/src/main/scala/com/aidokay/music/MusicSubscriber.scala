package com.aidokay.music

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}
import akka.util.ByteString
import com.aidokay.music.JokeBox.{JokeBoxState, Listener, MusicBox, SubscribeMusic}

object MusicSubscriber {

  sealed trait ListenMusic
  final case class StartPlayMusic(replyTo: ActorRef[ByteString]) extends ListenMusic {
    println(s"start play music replyTo: $replyTo")
    def newListener(): Listener = new Listener () {
      type O = Unit
      override def listen(chunk: Array[Byte]): O = {
        println("listen.....")
        replyTo ! ByteString(chunk)
      }
    }
  }

  def apply(musicStreamer: ActorRef[MusicBox]): Behavior[ListenMusic] = {
    Behaviors.receive { (context, message) =>
      context.log.info("receive: start playing music")
      message match {
        case pm @ StartPlayMusic(_) =>
          musicStreamer ! SubscribeMusic(pm.newListener())
      }
      Behaviors.same
    }
  }

}
