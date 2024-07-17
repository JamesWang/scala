package com.aidokay.ox

import ox.*
import ox.either.{catching, ok}

import scala.concurrent.duration.*

object Ex1 extends App {

  def computation1: Int = { sleep(2.seconds); 1}
  def computation2: String = {sleep(1.seconds); "2"}

  val result: (Int, String) = par(computation1, computation2)

  println(s"result = $result")

  def computation: Int = { sleep(2.seconds); 1 }

  val result2: Either[Throwable, Int] = catching(timeout(1.second)(computation))

  supervised {
    forkUser {
      sleep(1.second)
      println("Hello!")
    }
    forkUser {
      sleep(500.millis)
      throw new RuntimeException("boom!")
    }
  }
}
