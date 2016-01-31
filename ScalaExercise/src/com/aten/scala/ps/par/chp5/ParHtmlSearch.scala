package com.aten.scala.ps.par.chp5

/**
 * @author jooly
 */
import scala.concurrent._
import ExecutionContext.Implicits.global
import scala.io._
import scala.collection.GenSeq
import aten.util.Utility._

object ParHtmlSearch extends App {
 log(s" starting")
  def getHtmlSpec() = Future {
    val url = "http://www.w3.org/MarkUp/html-spec/html-spec.txt"
    val specSrc = Source.fromURL( url )
    try specSrc.getLines().toArray finally specSrc.close()
  }
 
  
  getHtmlSpec() foreach {
    case specDoc =>
      def search( d:GenSeq[String] ) : Double = 
        timed { 
          d.indexWhere(line => line.matches(".*TEXTAREA.*"))
        }
      val seqtime = search(specDoc)
      
      log(s" Sequential time $seqtime ms ")
      val partime = search(specDoc.par)
      log(s"Parallel time $partime ms")     
  }
  Thread.sleep(5000)
}