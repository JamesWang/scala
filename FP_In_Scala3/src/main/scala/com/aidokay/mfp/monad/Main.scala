package com.aidokay.mfp.monad

import com.aidokay.mfp.monad.MyMonad.Monad
import com.aidokay.mfp.writer.Logging
import com.aidokay.mfp.monad.MyMonad.map
import com.aidokay.mfp.monad.MyMonad.flatMap

object Main extends App {

  def add[F[_]](a: Double, b: Double)(using M: Monad[F], L: Logging[F]): F[Double] =
    for {
      _ <- L.log(s"Adding $a to $b")
      result = a + b
      _ <- L.log(s"The result of the operation is: $result")
    } yield result

  import Instances.writerMonad
  val result = add(1.0, 2.0)
  println(s"logging:  ${result.logs}")
  println(s"Result :  ${result.value}")
}
