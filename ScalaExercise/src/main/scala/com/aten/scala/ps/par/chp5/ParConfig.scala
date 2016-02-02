package com.aten.scala.ps.par.chp5

/**
 * @author jooly
 */

import scala.concurrent._
import ExecutionContext.Implicits.global
import scala.concurrent.forkjoin.ForkJoinPool
import scala.collection.parallel.ForkJoinTaskSupport
import scala.util.Random

object ParConfig extends App {
  val fjpool = new ForkJoinPool(2)
  val customTaskSupport = new ForkJoinTaskSupport(fjpool)
  val numbers = Random.shuffle(Vector.tabulate(5000000)( i=>i))
  import aten.util.Utility._
  
  val partime = timed {
    val parnumbers = numbers.par
    parnumbers.tasksupport = customTaskSupport
    val n = parnumbers.max
    
   log(s"largest number $n")
  }
  log(s" Parallel time $partime ms ")
}