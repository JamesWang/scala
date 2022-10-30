package com.aidokay.music

import akka.actor.Cancellable
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import com.aidokay.music.JokeBox._
import com.aidokay.music.JokeBoxData.JokeBoxContext
import com.aidokay.music.tracks.AudioProvider

import scala.concurrent.duration.DurationInt

class MusicStreamer(musicProvider: AudioProvider[String]) {
  private val jokeBoxData = new JokeBoxContext(musicProvider)

  private def streamAudioChunk(): Unit = {
    if (jokeBoxData.state() == Paused || jokeBoxData.isEmpty) return
    jokeBoxData.currentPlaying match {
      case None if jokeBoxData.isEmpty =>
        jokeBoxData.updateCurrentState(Paused)
      case None =>
        jokeBoxData.playNext()
      case Some(playing) =>
        playing.streamAudioChunk(subscribers)
    }
  }

  var subscribers: List[Listener] = Nil
  import scala.concurrent.ExecutionContext.Implicits.global

  def apply(): Behavior[JokeBoxState] = {
    var streamerInstance: Option[Cancellable] = None
    Behaviors.receive { (context, message) =>
      message match {
        case Playing | Paused =>
          jokeBoxData.updateCurrentState(Playing)
        case Scheduling(music) =>
          if (music.contains("all")) {
            context.log.info("scheduling all found music")
            jokeBoxData.allAudios()
          } else {
            context.log.info(s"scheduling $music")
            music.foreach(jokeBoxData.offer)
          }
        case SubscribeMusic(listener) =>
          subscribers :::= List(listener)
          if (subscribers.size == 1) {
            streamerInstance = Option(context.system.scheduler.scheduleAtFixedRate(
              100.millis,
              100.millis
            )(() => streamAudioChunk()))
          }
        case Cancel =>
          streamerInstance.foreach(_.cancel())
      }
      Behaviors.same
    }
  }
}
