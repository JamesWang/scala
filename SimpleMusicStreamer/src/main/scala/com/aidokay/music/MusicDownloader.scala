package com.aidokay.music

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.{ActorContext, Behaviors}
import com.aidokay.music.JokeBox.{DownloadMusic, Downloaded, MusicBox}
import com.aidokay.music.tracks.AudioProvider

class MusicDownloader(audioProvider: AudioProvider[String]) {

  def apply(): Behavior[MusicBox] = {
    Behaviors.receive { (context, message) =>
      implicit val ctx: ActorContext[MusicBox] = context
      message match {
        case DownloadMusic(track, replyTo) =>
          replyTo ! Downloaded(audioProvider.location + "/" + track)
      }
      Behaviors.same
    }
  }

}
