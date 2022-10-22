package com.aidokay.music

object Commands {
  trait Command

  case object ListMusic extends Command
  case object PlayMusic extends Command
  case object Pause extends Command

  case class Schedule(tracks: List[String]) extends Command
}
