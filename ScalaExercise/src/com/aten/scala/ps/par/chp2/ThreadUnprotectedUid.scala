package com.aten.scala.ps.par.chp2

/**
 * @author jooly
 */
object ThreadUnprotectedUid extends App {

  var uidCount = 0L
  def getUniqueId() = {
    val freshUid = uidCount + 1
    uidCount = freshUid
    freshUid
  }
  import aten.util.Utility._
  def printUniqueIds(n: Int): Unit = {
    val uids = for (i <- 0 until n) yield getUniqueId
    Console.println(s"Generated uids: $uids")
  }
  val t = thread { printUniqueIds(5) }
  printUniqueIds(5)
  t.join
}