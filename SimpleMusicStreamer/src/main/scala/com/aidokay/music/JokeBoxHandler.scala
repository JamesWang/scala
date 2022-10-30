package com.aidokay.music

import akka.actor.ActorRef
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.{ActorContext, Behaviors}
import com.aidokay.music.JokeBox._
import com.aidokay.music.tracks.AudioProvider

class JokeBoxHandler(audioProvider: AudioProvider[String]) {

  private val musicStreamer = new MusicStreamer(audioProvider).apply()

  def play()(implicit ctx: ActorContext[MusicBox]): Unit = {

  }
  def pause()(implicit ctx: ActorContext[MusicBox]): Unit = {}

  def list(replyTo: ActorRef)(implicit ctx: ActorContext[MusicBox]): Unit = {
    ctx.log.info("in list()")
    replyTo ! ListedMusic(audioProvider.audioList())
  }

  def schedule(tracks: List[String], replyTo: ActorRef)(implicit ctx: ActorContext[MusicBox]): Unit = {
    if (tracks.contains("all")) {} else if (tracks.isEmpty) {} else {}
  }
  def apply(): Behavior[MusicBox] = {
    Behaviors.receive { (context, message) =>
      implicit val ctx: ActorContext[MusicBox] = context
      context.log.info(s"Received: $message")
      message match {
        case ListMusic(replyTo)             => list(replyTo)
        case PlayMusic(_)                   => play()
        case PauseMusic(_)                  => pause()
        case ScheduleMusic(tracks, replyTo) => schedule(tracks, replyTo)
        case Ignore                         => ()
      }
      Behaviors.same
    }
  }
}
