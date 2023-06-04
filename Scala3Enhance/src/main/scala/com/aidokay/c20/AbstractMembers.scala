package com.aidokay.c20

object AbstractMembers {

  trait Abstract:
    type T
    def transform(x:T):T
    val initial: T
    var current: T

  class Concrete extends Abstract:
    type T = String

    override def transform(x: String): String = x + x

    override val initial: String = "hi"
    var current: String = initial

    abstract class Fruit:
      val v: String
      def m: String

    abstract class Apple extends Fruit:
      val v: String
      val m: String
}
