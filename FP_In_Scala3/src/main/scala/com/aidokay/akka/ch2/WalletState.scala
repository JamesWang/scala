package com.aidokay.akka.ch2

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors

object WalletState {
  sealed trait Command
  final case class Increase(amount: Int) extends Command
  final case class Decrease(amount: Int) extends Command

  def apply(total: Int, max: Int): Behavior[Command] =
    Behaviors.receive{(context, message) =>
      message match
        case Increase(amt) =>
          val current = total + amt
          if (current < max) {
            context.log.info(s"Increasing to $current")
            apply(current, max)
          } else {
            context.log.info(s"I'm overloaded. Counting '$current' while max is '$max'. Stopping'")
            Behaviors.stopped
          }
        case Decrease(amt) =>
          val current = total - amt
          if (current < 0 ) {
            context.log.info("Cannot run below zero, Stopping")
            Behaviors.stopped
          } else {
            context.log.info(s"decreasing to $current")
            apply(current, max)
          }

    }
}
