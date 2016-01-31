package com.aten.scala.ps.par.chp4.future

/**
 * @author jooly
 */
object FuturesCreate extends App {
  import scala.concurrent._
  import ExecutionContext.Implicits.global
  Future {
    Console.println( " The future is here ")
  }
  Console.println(" The future is coming")
  Thread.sleep(1000)
}