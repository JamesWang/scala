package com.aidokay.zio2

import zio.{FiberId, ZIO}

trait WithDebug {
  def debugInterruption(taskName: String) = (fibers: Set[FiberId]) =>
    for {
      fn <- ZIO.fiberId.map(_.threadName)
      _ <- ZIO.debug(s"The $fn fiber which is the underlying fiber of the '$taskName' task interrupted by ${fibers.map(_.threadName).mkString(",")}")
    } yield ()
}
