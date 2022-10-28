package com.aidokay.music

import akka.actor.ActorRef
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import com.aidokay.music.JokeBox._
import com.aidokay.music.tracks.AudioProvider

class JokeBoxHandler(audioProvider: AudioProvider[String]) {

  private val jokeBoxData = new JokeBoxData[String](audioProvider)

  def play(): Unit = {
    jokeBoxData.updateCurrentState(Playing)
  }
  def pause(): Unit = {
    jokeBoxData.updateCurrentState(Paused)
  }

  def list(replyTo: ActorRef): Unit = {
    println("in list()")
    // audioProvider.audios()
    replyTo ! ListedMusics(List("hello.mp3", "welcome.mp3"))
  }

  def schedule(tracks: List[String], replyTo: ActorRef): Unit = {
    if (tracks.contains("all")) {} else if (tracks.isEmpty) {} else {}
  }
  def apply(): Behavior[MusicBox] = {
    Behaviors.receive { (context, message) =>
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
