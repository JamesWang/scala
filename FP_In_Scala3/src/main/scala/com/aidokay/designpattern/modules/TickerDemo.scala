package com.aidokay.designpattern.modules

object TickerDemo {

  trait Tick:
    trait Ticker:
      def count(): Int

      def tick(): Unit

    def ticker: Ticker


  trait TickUser extends Tick:
    class TickUserImpl extends Ticker:
      var current = 0

      override def count(): Int = current

      override def tick(): Unit = current += 1

    object ticker extends TickUserImpl

  trait Alarm:
    trait Alarmer:
      def trigger(): Unit

    def alarm: Alarmer

  trait AlarmUser extends Alarm with Tick:
    class AlarmUserImpl extends Alarmer:
      override def trigger(): Unit =
        if (ticker.count() % 10 == 0) {
          println(s"Alarm triggered at ${ticker.count()}!")
        }

    object alarm extends AlarmUserImpl
}
