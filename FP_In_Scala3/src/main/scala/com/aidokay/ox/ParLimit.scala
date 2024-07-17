package com.aidokay.ox

import ox.{parLimit, sleep}

import scala.concurrent.duration.*

object ParLimit extends App {

  def computation(n: Int): Int =
    sleep(1.second)
    println(s"Running $n")
    n * 2

  val computations: Seq[() => Int] = (1 to 20).map(n => () => computation(n))
  val result: Seq[Int] = parLimit(5)(computations)
  println(s"result=$result")
}
