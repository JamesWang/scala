package com.aten.scala.ps.par.chp5

/**
 * @author jooly
 */

import scala.concurrent._
import aten.util.Utility._
import scala.util.Random

object ParBasic extends App {
  val numbers = Random.shuffle( Vector.tabulate(5000000)(i => i) )
  val seqTime = timed{ numbers.max }
  log(s" Sequential time $seqTime ms")
  val  partime = timed( numbers.par.max )
  log(s"Parallel time $partime ms")
}