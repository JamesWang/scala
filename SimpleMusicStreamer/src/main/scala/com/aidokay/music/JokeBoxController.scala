package com.aidokay.music

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import com.aidokay.music.Commands.{
  Command,
  ListMusic,
  Pause,
  PlayMusic,
  Schedule
}
import com.aidokay.music.JokeBox.{ListM, MusicBox, PauseM, PlayM, ScheduleM}

object JokeBoxController {

  object JokeBoxHandler {
    def apply(): Behavior[MusicBox] = Behaviors.receive { (context, message) =>
      context.log.info(s"Received: $message")
      Behaviors.same
    }
  }

  def apply(): Behavior[Command] = Behaviors.setup { context =>
    val handler = context.spawn(JokeBoxHandler(), "jokeBoxHandler")
    Behaviors.receiveMessage {
      case ListMusic =>
        handler ! ListM(context.self)
        Behaviors.same
      case PlayMusic =>
        handler ! PlayM(context.self)
        Behaviors.same
      case Pause =>
        handler ! PauseM(context.self)
        Behaviors.same
      case Schedule(tracks) =>
        handler ! ScheduleM(tracks, context.self)
        Behaviors.same
    }
  }
}
