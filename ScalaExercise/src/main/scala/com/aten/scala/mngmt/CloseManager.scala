package com.aten.scala.mngmt

import scala.util.control.NonFatal

object CloseManager {
  def apply[R <: { def close(): Unit }, T](resource: => R)(f: R => T) = {
    var res: Option[R] = None
    try {
      res = Some(resource)
      f(res.get)
    } catch {
      case NonFatal(ex) => println(s" Non fatal exception, $ex")
    } finally {
      if (res != None) {
        println("Closing resource")
        res.get.close()
      }
    }
  }
}