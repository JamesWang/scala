package com.aidokay.music

import akka.actor.typed.ActorRef

object JokeBox {
  case object Controller

  sealed trait MusicBox {
    val replyTo: ActorRef[_]
  }
  final case class ListM(replyTo: ActorRef[_]) extends MusicBox
  final case class PlayM(replyTo: ActorRef[_]) extends MusicBox
  final case class PauseM(replyTo: ActorRef[_]) extends MusicBox
  final case class ScheduleM(tracks: List[String], replyTo: ActorRef[_]) extends MusicBox
}
