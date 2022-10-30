package com.aidokay.makro.summon

import com.aidokay.makro.summon.Debugger.hello

object TestIt extends App {

  hello()

  val x = 0
  val y = 1

  println("--")

  Debugger.debugSingle(x)
  Debugger.debugSingle(x + y)
  Debugger.debug(x + 10)
}
