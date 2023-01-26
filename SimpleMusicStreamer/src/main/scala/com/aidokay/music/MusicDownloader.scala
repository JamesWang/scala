package com.aidokay.music

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.{ActorContext, Behaviors}
import akka.stream.scaladsl.{FileIO, Source}
import akka.util.ByteString
import com.aidokay.music.JokeBox.{DownloadInfo, DownloadMusic, MusicBox, TrackLocation}
import com.aidokay.music.tracks.AudioProvider

import java.nio.file.Paths
object MusicDownloader {

  class TrackInfoProvider(audioProvider: AudioProvider[String]) {

    def apply(): Behavior[MusicBox] = {
      Behaviors.receive { (context, message) =>
        implicit val ctx: ActorContext[MusicBox] = context
        message match {
          case DownloadMusic(track, replyTo) =>
            replyTo ! DownloadInfo(TrackLocation(audioProvider.location), track)
        }
        Behaviors.same
      }
    }
  }

  def trackContentSource(trackInfo: String): Source[ByteString, _] =
    FileIO.fromPath(Paths.get(trackInfo))

}
