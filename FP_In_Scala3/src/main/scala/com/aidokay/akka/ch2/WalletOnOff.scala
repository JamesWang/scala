package com.aidokay.akka.ch2

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors

object WalletOnOff {
  sealed trait Command

  final case class Increase(amount: Int) extends Command

  case object Activate extends Command

  case object Deactivate extends Command

  def apply(): Behavior[Command] = activated(0)

  def activated(total: Int): Behavior[Command] =
    Behaviors.receive{(context, message) =>
      message match
        case Increase(amount) =>
          val current = total + amount
          context.log.info(s"increasing to $current")
          activated(current)
        case Deactivate =>
          deactivated(total)
        case Activate =>
          Behaviors.same
    }

  def deactivated(total: Int): Behavior[Command] =
    Behaviors.receive { (context, message) =>
      message match
        case Increase(_) =>
          context.log.info(s"Wallet is deactivated. Cannot increase")
          Behaviors.same
        case Deactivate =>
          Behaviors.same
        case Activate =>
          context.log.info("activating")
          activated(total)
    }
}
