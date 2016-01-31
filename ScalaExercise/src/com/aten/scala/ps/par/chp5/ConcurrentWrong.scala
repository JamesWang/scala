package com.aten.scala.ps.par.chp5

/**
 * @author jooly
 */
import aten.util.Utility._
import scala.concurrent._
import ExecutionContext.Implicits.global
import scala.collection._


object ConcurrentWrong extends App {
  def intersection( a: GenSet[String], b:GenSet[String]) = {
    val result = new mutable.HashSet[String]
    for( x<-a.par) if( b.contains(x)) result.add(x)
    result
  }
  import java.util.concurrent.ConcurrentSkipListSet
  import scala.collection.convert.decorateAsScala._
  
  def intersection2( a:GenSet[String], b:GenSet[String]) = {
    val skipList = new ConcurrentSkipListSet[String]
    for( x<-a.par ) if( b.contains(x) ) skipList.add(x)
    val result : Set[String] = skipList.asScala
    result
  }
  import com.aten.scala.ps.par.chp4.future.FuturesCallbacks.getUrlSpec
  val ifut = for  {
    htmlSpec <- ParHtmlSearch.getHtmlSpec
    urlSpec  <- getUrlSpec
  } yield {
    val htmlWords = htmlSpec.mkString.split("\\s+").toSet
    val urlWords  = urlSpec.mkString.split("\\s+").toSet
    intersection2( htmlWords, urlWords )
  }
  ifut.onComplete { case t => log(s" Result: $t ")}
  
  Thread.sleep(3000)
}