package com.aidokay.music

import akka.actor.ActorRef

object JokeBox {
  case object Controller

  sealed trait JokeBoxState
  case object Playing extends JokeBoxState
  case object Paused extends JokeBoxState

  sealed trait MusicBox
  final case class ListMusic(replyTo: ActorRef) extends MusicBox
  final case class PlayMusic(replyTo: ActorRef) extends MusicBox
  final case class PauseMusic(replyTo: ActorRef) extends MusicBox
  final case class ScheduleMusic(tracks: List[String], replyTo: ActorRef) extends MusicBox
  final case class ListedMusic(music: List[String]) extends MusicBox
  final case object Ignore extends MusicBox
  final case object Cancel extends MusicBox
  final case class SubscribeMusic(listener: Listener) extends MusicBox

  trait Listener {
    type O
    def listen(chunk: Array[Byte]): O
  }
}
