package com.aten.scala.ps.par.chp5

/**
 * @author jooly
 */

import aten.util.Utility._
import scala.concurrent._
import ExecutionContext.Implicits.global
import scala.collection._
object ConcurrentTrieMap extends App {
  val cache = new concurrent.TrieMap[Int,String]
  for( i <- 0 until 100 ) cache(i) = i.toString()
  for( (n, st) <- cache.par) cache( -n ) = s" -$st"
  log(s"cache - ${cache.keys.toList.sorted}")
  
  log(s"cache - ${cache.values.toList.sorted}")
}
