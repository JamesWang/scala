package com.aidokay.music

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.{ActorContext, Behaviors}
import akka.actor.{ActorRef, Cancellable}
import akka.stream.scaladsl.Source
import akka.util.ByteString
import com.aidokay.music.JokeBox._
import com.aidokay.music.JokeBoxData.JokeBoxContext
import com.aidokay.music.tracks.AudioProvider

import scala.concurrent.duration.DurationInt

class JokeBoxHandler(audioProvider: AudioProvider[String]) {

  private val jokeBoxData = new JokeBoxContext(audioProvider)

  private def streamAudioChunk(): Iterator[ByteString] =
    new Iterator[ByteString] {
      override def hasNext: Boolean = true

      jokeBoxData.currentPlaying.map(_.currentPlay.streamAudioChunk())

      override def next(): ByteString = {
        if (jokeBoxData.state() == Paused) return ByteString.empty
        jokeBoxData.currentPlaying match {
          case None if jokeBoxData.isEmpty =>
            jokeBoxData.updateCurrentState(Paused)
            ByteString.empty
          case None =>
            jokeBoxData.playNext()
            ByteString.empty
          case Some(playing) =>
            if (playing.currentPlay.isDone && jokeBoxData.isEmpty) {
              jokeBoxData.stopPlaying()
              ByteString.empty
            } else {
              val chunk = playing.chunks.next()
              if (chunk.isEmpty) {
                playing.currentPlay.close()
                jokeBoxData.stopPlaying()
              }
              chunk
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

  val timedSource: Source[() => ByteString, Cancellable] = Source.tick(1.second, 200.millisecond, streamAudioChunk().next)

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
        case SubscribeMusic(replyTo) =>
          context.log.info(s"SubscribeMusic from [$replyTo")
          replyTo ! Subscribed(timedSource)
        case Cancel =>
          streamerInstance.foreach(_.cancel())
      }
      Behaviors.same
    }
  }
}
