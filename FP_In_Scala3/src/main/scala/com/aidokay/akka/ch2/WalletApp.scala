package com.aidokay.akka.ch2

import akka.actor.typed.ActorSystem

object WalletApp extends App with Terminator {
  private val guardian: ActorSystem[Int] = ActorSystem(Wallet(), "wallet")
  guardian ! 1
  guardian ! 10

  guardian.enterToTerminate()
}
