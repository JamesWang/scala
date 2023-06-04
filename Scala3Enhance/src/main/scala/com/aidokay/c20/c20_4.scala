package com.aidokay.c20

import scala.annotation.tailrec

//class parameter arguments are evaluated before it is passed to the class constructor(unless by-name)
//an implementing val definition in a subclass, is evaluated only after the superclass has been initialized
object c20_4 extends App {
  trait RationalTrait1 {
    //abstract val
    val numerArg: Int
    val denomArg: Int

    //require(denomArg != 0)

    private lazy val g = gcd(numerArg, denomArg)
    lazy val numer: Int = numerArg / g
    lazy val denom: Int = denomArg / g

    @tailrec
    private def gcd(a: Int, b: Int): Int = if (b == 0) a else gcd(b, a % b)

    override def toString: String = s"$numer/$denom"
  }

  val x = 2
  //anonymous class is initialized after the RationalTrait
  //that is, both numerArg and denomArg get the values after
  //RationalTrait1 initialized, so the require(denomArg !=0 ) not pass
  new RationalTrait1 :
    override val numerArg: Int = 1 * x
    override val denomArg: Int = 2 * x
  
}