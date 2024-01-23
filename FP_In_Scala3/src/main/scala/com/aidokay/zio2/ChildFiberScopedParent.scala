package com.aidokay.zio2

import zio.*

object ChildFiberScopedParent extends ZIOAppDefault, WithDebug{
  
  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] =
    for {
      fn <- ZIO.fiberId.map(_.threadName)
      _ <- ZIO.debug(s"$fn starts working.")
      child = for {
        cfn <- ZIO.fiberId.map(_.threadName)
        _ <- ZIO.debug(s"$cfn (a child) starts working by forking from its parent ($fn)")
        _ <- ZIO.never
      } yield ()
      _ <- child.onInterrupt(debugInterruption("child")).fork
      _ <- ZIO.sleep(1.second)
      _ <- ZIO.debug(s"$fn finishes its job and is going to go exit, which will interrupt all its children if they are not joined or not completed yet")
    } yield ()

}
