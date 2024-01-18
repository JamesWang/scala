package com.aidokay.retirement.tf

import cats.Applicative

object TaglessFinal {

  trait Algebra[E[_]] {
    def b(boolean: Boolean): E[Boolean]
    def i(int: Int): E[Int]
    def or(left: E[Boolean], right: E[Boolean]): E[Boolean]
    def and(left: E[Boolean], right: E[Boolean]): E[Boolean]
    def sum(left: E[Int], right: E[Int]): E[Int]
  }

  case class SimpleExpr[A](value: A)

  given Algebra[SimpleExpr] with
    override def b(boolean: Boolean): SimpleExpr[Boolean] = SimpleExpr(boolean)

    override def or(left: SimpleExpr[Boolean], right: SimpleExpr[Boolean]): SimpleExpr[Boolean] = SimpleExpr(left.value || right.value)

    override def and(left: SimpleExpr[Boolean], right: SimpleExpr[Boolean]): SimpleExpr[Boolean] = SimpleExpr(left.value && right.value)

    override def i(int: Int): SimpleExpr[Int] = SimpleExpr(int)

    override def sum(left: SimpleExpr[Int], right: SimpleExpr[Int]): SimpleExpr[Int] = SimpleExpr(left.value + right.value)


  def program1[E[_]](using alge: Algebra[E]) =
    import alge._
    or(b(true), and(b(false), b(true)))

  def program2[E[_]](using alge: Algebra[E]) =
    import alge._

    sum(i(1), i(10))


  def demoTF(): Unit =
    println(program1[SimpleExpr].value)
    println(program2[SimpleExpr].value)

  def main(args: Array[String]): Unit = {
    demoTF()
  }
}
