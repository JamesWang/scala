package com.aten.scala.ps.par.chp5

/**
 * @author jooly
 */
import aten.util.Utility._
import scala.concurrent._
import ExecutionContext.Implicits.global

object ParNonDeterminsticOperation extends App {
  ParHtmlSearch.getHtmlSpec() foreach { specDoc =>
    val pattern = ".*TEXTAREA.*"
    val seqresult = specDoc.find { _.matches(pattern) }
    val parresult = specDoc.par.find { _.matches(pattern)}
    
    log(s" Sequential result - $seqresult")
    log(s" Parallel result   - $parresult")
  }
  Thread.sleep(4000)
    
}