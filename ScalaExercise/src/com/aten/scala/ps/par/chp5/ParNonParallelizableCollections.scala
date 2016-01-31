package com.aten.scala.ps.par.chp5

/**
 * @author jooly
 */
object ParNonParallelizableCollections extends App {
  val list = List.fill( 10000000)("")
  val vector = Vector.fill( 1000000 )("")
  
  import aten.util.Utility._
  
  log(s" list conversion time: ${timed(list.par)} ms ")
  log(s" vector conversion time: ${timed(list.par)} ms ")
  
}