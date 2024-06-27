package com.aidokay.akka.ch2

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors

object Wallet {
  def apply(): Behavior[Int] =
    Behaviors.receive { (context, message) =>
      context.log.info(s"received '$message' dollar(s)'")
      Behaviors.same
    }
}
