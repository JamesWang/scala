package com.aten.scala.ps.par.chp5

import java.util.concurrent.atomic._

/**
 * @author jooly
 */
object ParUid extends App {
  import aten.util.Utility._
  private val uid = new AtomicLong(0L)
  val seqtime = timed {
    for( i <- ( 0 until 10000000)) uid.incrementAndGet()
  }
  log(s" Sequential time $seqtime ms ")
  
  val parTime = timed {
    for( i <- (0 until 10000000).par) uid.incrementAndGet()
  }
  log(s" Parallel time $parTime ms ")
}