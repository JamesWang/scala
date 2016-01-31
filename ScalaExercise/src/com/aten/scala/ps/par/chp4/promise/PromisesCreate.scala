package com.aten.scala.ps.par.chp4.promise

/**
 * @author jooly
 */
import scala.concurrent._
import ExecutionContext.Implicits.global
import aten.util.Utility._

object PromisesCreate extends App {
  val p = Promise[String]  
  val q = Promise[String]

  //Install a callback on the future of p
  p.future foreach {
    case x => log(s" p succeeded with '$x'") 
  }
  Thread.sleep(1000)
  //Assign value to the future using Promise
  //p.success( "assigned ")
  p.trySuccess("assigned - by trySuccess")
  q.failure( new Exception("not kept"))
  q.future.failed foreach { case t => log(s" q failed with $t") }
  
  Thread.sleep(1000)
}