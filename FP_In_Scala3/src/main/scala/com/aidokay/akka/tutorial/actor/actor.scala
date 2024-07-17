package com.aidokay.akka.tutorial.actor

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, ActorSystem, Behavior}

object actor extends App {
  object Printer {
    case class PrintMe(message: String)

    def apply(): Behavior[PrintMe] = Behaviors.receive {
      case (context, PrintMe(message)) =>
        context.log.info(message)
        Behaviors.same
    }
  }

  val system = ActorSystem(Printer(), "fire-and-forget-sample")
  val printer: ActorRef[Printer.PrintMe] = system

  printer ! Printer.PrintMe("message 1")
  printer ! Printer.PrintMe("not message 2")
  system.terminate()
}