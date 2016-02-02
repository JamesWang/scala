package com.aten.scala.ps.par.chp5

/**
 * @author jooly
 */
import aten.util.Utility._
import scala.concurrent._
import ExecutionContext.Implicits.global
import scala.collection._

object ParNonCommutativeOperator extends App {

  val doc = mutable.ArrayBuffer.tabulate(20)( i=> s"Page $i,")
  def test( doc: GenIterable[String]) {
    val seqtext = doc.seq.reduceLeft( _+_)
    val partext = doc.par.reduce( _+_)
    
    log(s" Sequential result - $seqtext\n")
    log(s" Parallel   result - $partext\n")
  }
  test(doc)
  test(doc.toSet)
  
}