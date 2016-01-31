package com.aten.scala.ps.par.chp4.future

/**
 * @author jooly
 */
import scala.concurrent._
import ExecutionContext.Implicits.global
import scala.io._
import aten.util.Utility._

object FuturesFailure extends App {
  val urlSpec :Future[String] = Future {
    val invalidUrl = "http://www.w3.org/non-existent.html"
    Source.fromURL(invalidUrl) mkString("\n")    
  }  
  
  urlSpec.failed foreach {
    case t => log(s"exception occured - $t")
  }
  Thread.sleep(1000)
}