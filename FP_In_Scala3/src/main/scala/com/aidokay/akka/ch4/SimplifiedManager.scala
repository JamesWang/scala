package com.aidokay.akka.ch4

import akka.actor.typed.scaladsl.Behaviors

object SimplifiedManager {
  sealed trait Command
  final case class CreateChild(name: String) extends Command
  
  def apply(): Behaviors.Receive[Command] =
    Behaviors.receive{(context, message) =>
      message match
        case CreateChild(name) =>
          context.spawn(SimplifiedWorker(), name)
          Behaviors.same
    }

  
}
