package com.aidokay.zio2

import zio.{Scope, ZIO, ZIOAppArgs, ZIOAppDefault}

object ZioMainApp1 extends ZIOAppDefault {
  def run1: ZIO[ZIOAppArgs with Scope, String, Nothing] = ZIO.succeed(5) *> ZIO.fail("Oh uh!")

  private def run2: ZIO[ZIOAppArgs with Scope, String, Int] = ZIO.succeed(5) <* ZIO.fail("Oh uh!")

  def run3 = ZIO.die(new ArithmeticException("divide by zero"))

  def run4 = ZIO
    .attempt(throw new StackOverflowError("The call stack pointer exceeds the stack bound."))
    .catchAll(_ => ZIO.unit)
    .catchAllDefect(_ => ZIO.unit)

  def run = run4
}
