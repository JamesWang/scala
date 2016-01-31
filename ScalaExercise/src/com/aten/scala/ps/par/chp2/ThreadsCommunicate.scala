package com.aten.scala.ps.par.chp2

/**
 * @author jooly
 */
import aten.util.Utility._
object ThreadsCommunicate extends App {
  var result: String = null
  //val t = new Thread{ result = "\nTitle\n" + "=" * 5 }
  val t = thread { result = "\nTitle\n" + "=" * 5 }
  t.join
  Console.print(result)
}