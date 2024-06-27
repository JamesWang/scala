package com.aidokay.akka.ch2

import akka.actor.typed.ActorSystem

trait Terminator {
  extension (guardian: ActorSystem[_])
    def enterToTerminate(): Unit = {
      println("Press Enter to terminate")
      scala.io.StdIn.readLine()
      guardian.terminate()
    }
}
