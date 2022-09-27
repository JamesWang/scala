package com.aidokay.scala_test

trait Calculator[T] {
  def add(a: T, b: T): T
}

object CalculatorInstances {
  implicit val intCalculator: Calculator[Int] = (a: Int, b: Int) => a + b

  implicit val doubleCalculator: Calculator[Double] = (a: Double, b: Double) => a + b

  implicit val bigDecimalCalculator: Calculator[BigDecimal] = (a: BigDecimal, b: BigDecimal) => a.+(b)
}

object Calculator {
  def addTwo[T: Calculator](a: T, b: T): T = implicitly[Calculator[T]].add(a, b)
}