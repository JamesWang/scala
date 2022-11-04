package com.aidokay.music

import akka.actor.{ActorRef, Cancellable}
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.{ActorContext, Behaviors}
import com.aidokay.music.JokeBox._
import com.aidokay.music.JokeBoxData.JokeBoxContext
import com.aidokay.music.tracks.AudioProvider

import scala.concurrent.duration.DurationInt

class JokeBoxHandler(audioProvider: AudioProvider[String]) {

  private val jokeBoxData = new JokeBoxContext(audioProvider)
  var subscribers: List[Listener] = Nil

  import scala.concurrent.ExecutionContext.Implicits.global

  private def streamAudioChunk(): Unit = {
    if (jokeBoxData.state() == Paused || jokeBoxData.isEmpty) return
    jokeBoxData.currentPlaying match {
      case None if jokeBoxData.isEmpty =>
        jokeBoxData.updateCurrentState(Paused)
      case None =>
        jokeBoxData.playNext()
      case Some(playing) =>
        if (playing.isDone && jokeBoxData.isEmpty) {
          jokeBoxData.stopPlaying()
        }else {
          playing.streamAudioChunk(subscribers)
          if (playing.isDone) {
            jokeBoxData.stopPlaying()
          }
        }
    }
  }

  def play()(implicit ctx: ActorContext[MusicBox]): Unit = {
    jokeBoxData.updateCurrentState(Playing)
  }
  def pause()(implicit ctx: ActorContext[MusicBox]): Unit = {
    jokeBoxData.updateCurrentState(Paused)
  }

  def list(replyTo: ActorRef)(implicit ctx: ActorContext[MusicBox]): Unit = {
    replyTo ! ListedMusic(audioProvider.audioList())
  }

  def schedule(tracks: List[String], replyTo: ActorRef)(implicit
      ctx: ActorContext[MusicBox]
  ): Unit = {
    if (tracks.contains("all")) {
      ctx.log.info("scheduling all found music")
      jokeBoxData.allAudios()
    } else {
      ctx.log.info(s"scheduling $tracks")
      tracks.foreach(jokeBoxData.offer)
    }
  }
  def apply(): Behavior[MusicBox] = {
    var streamerInstance: Option[Cancellable] = None
    Behaviors.receive { (context, message) =>
      implicit val ctx: ActorContext[MusicBox] = context
      context.log.info(s"Received: $message")
      message match {
        case ListMusic(replyTo)             => list(replyTo)
        case PlayMusic(_)                   => play()
        case PauseMusic(_)                  => pause()
        case ScheduleMusic(tracks, replyTo) => schedule(tracks, replyTo)
        case Ignore                         => ()
        case SubscribeMusic(listener) =>
          context.log.info(s"SubscribeMusic[$listener")
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
