package com.aidokay.music

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}
import com.aidokay.music.JokeBox.{Listener, MusicBox, SubscribeMusic}

object MusicSubscriber {

  sealed trait ListenMusic

  final case class StartPlayMusic(listener: Listener) extends ListenMusic

  def apply(musicStreamer: ActorRef[MusicBox]): Behavior[ListenMusic] = {
    Behaviors.receive { (context, message) =>
      context.log.info("receive: start playing music")
      message match {
        case pm @ StartPlayMusic(_) =>
          musicStreamer ! SubscribeMusic(pm.listener)
      }
      Behaviors.same
    }
  }

}
