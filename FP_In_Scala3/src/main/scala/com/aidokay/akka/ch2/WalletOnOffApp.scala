package com.aidokay.akka.ch2

import akka.actor.typed.ActorSystem

object WalletOnOffApp extends App with Terminator {
  val guardian: ActorSystem[WalletOnOff.Command] = ActorSystem(WalletOnOff(), "wallet-on-off")
  guardian ! WalletOnOff.Increase(1)
  guardian ! WalletOnOff.Deactivate
  guardian ! WalletOnOff.Increase(1)
  guardian ! WalletOnOff.Activate
  guardian ! WalletOnOff.Increase(1)

  guardian.enterToTerminate()

}
