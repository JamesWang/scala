package com.aten.scala.ps.par.chp4.future

/**
 * @author jooly
 */
import scala.concurrent._
import ExecutionContext.Implicits.global
import scala.io._
import aten.util.Utility._

object FuturesCallbacks extends App {
  def getUrlSpec():Future[List[String]] = Future {
    val url = "http://www.w3.org/Addressing/URL/url-spec.txt"
    val f = Source.fromURL(url)
    try f.getLines().toList finally f.close()
  } 
  val urlSpec :Future[List[String]] = getUrlSpec
  def find(lines:List[String], keyword:String):String =
    lines.zipWithIndex collect {
      case (line,n) if line.contains(keyword) =>(n,line)    
    } mkString("\n")
  
  urlSpec.foreach { 
      case lines => log(find(lines,"telnet")) 
  }
  urlSpec.foreach { 
      case lines => log(find(lines,"password")) 
  }
  log("callback registered, continuing with other work")
  Thread.sleep(2000)
}