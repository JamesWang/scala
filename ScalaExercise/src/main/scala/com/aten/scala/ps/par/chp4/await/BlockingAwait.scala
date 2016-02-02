package com.aten.scala.ps.par.chp4.await

/**
 * @author jooly
 */
import scala.concurrent._
import scala.concurrent.duration._
import ExecutionContext.Implicits.global
import aten.util.Utility._
import scala.io._
object BlockingAwait extends App {

  val urlSpecSizeFuture = Future {
    val specUrl = "http://www.w3.org/Addressing/URL/url-spec.txt"
    Source.fromURL( specUrl ).size  
  }
  val urlSpecSize = Await.result(urlSpecSizeFuture, 10.seconds )
  log(s"url spec contains $urlSpecSize characters")
  
  val startTime = System.nanoTime()
  val futures = for( _ <-0 until 16 ) yield Future {
    blocking {
      Thread.sleep(1000)
    }
  }
  for( f <- futures ) Await.ready(f, Duration.Inf)
  val endTime = System.nanoTime()
  log(s" Total time = ${(endTime-startTime)/1000000} ms ")
  log(s"Total CPUs = ${Runtime.getRuntime.availableProcessors()}")
}