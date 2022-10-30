package com.aidokay.makro.summon

import scala.compiletime.summonFrom

object SummonFrom {

  trait A

  trait B

  inline def trySummonFrom(label: String, expected: Int): Unit = {
    val actual = summonFrom {
      case given A => 1
      case given B => 2
      case _ => 0
    }
    printf("%-9s trySummonFrom(): %s =?= %d\n", label, expected, actual)
  }

  def tryNone(): Unit = trySummonFrom("tryNone", 0)

  def tryA(): Unit =
    given A with {}
    trySummonFrom("tryA", 1)

  def tryB(): Unit =
    given B with {}
    trySummonFrom("tryB", 2)

  def tryAB(): Unit =
    given A with {}
    given B with {}
    trySummonFrom("tryAB", 1)

  def main(args: Array[String]): Unit = {
    tryNone()
    tryA()
    tryB()
    tryAB()
  }
}
