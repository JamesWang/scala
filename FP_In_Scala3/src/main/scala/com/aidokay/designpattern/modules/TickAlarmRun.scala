package com.aidokay.designpattern.modules

import com.aidokay.designpattern.modules.TickerDemo.{AlarmUser, TickUser}

object TickAlarmRun extends AlarmUser with TickUser {

  def main(args: Array[String]): Unit = {
    println("Running the ticker. Should trigger the alarm every 10 times.")

    (1 to 100) foreach { _ =>
      ticker.tick()
      alarm.trigger()
    }
  }
}
