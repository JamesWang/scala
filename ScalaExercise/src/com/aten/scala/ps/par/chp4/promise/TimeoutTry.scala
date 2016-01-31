package com.aten.scala.ps.par.chp4.promise

/**
 * @author jooly
 */
import aten.util.Utility.FutureOps
import aten.util.Utility.MyTimer._
import scala.concurrent._
import ExecutionContext.Implicits.global

object TimeoutTry extends App {
  val f = timeout(1000).map { _ =>"timeout" } or Future {
    Thread.sleep(800)
    "Work Completed"
  }
  f.foreach { x=>aten.util.Utility.log(x)}
  Thread.sleep(2000)
}