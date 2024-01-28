package com.aidokay.mfp.kleisli

import scala.annotation.targetName

object KleisliEx {
  type Writer[A] = (A, String)

  def pure[A](a: A): Writer[A] = (a, "")

  object kleisli {
    //implicit class KleisliOps[A, B](m1: A => Writer[B]) {
    extension [A, B](m1: A => Writer[B])
      @targetName("kleisli arrow")
      def >=>[C](m2: B => Writer[C]): A => Writer[C] = {
        a => {
          val (b, s1) = m1(a)
          val (c, s2) = m2(b)
          (c, s1 + s2)
        }
      }
  }

  import kleisli.*

  def fmap[A, B](f: A => B): Writer[A] => Writer[B] =
    identity[Writer[A]] _ >=> (a => pure(f(a)))

  def bind[A, B](f: A => Writer[B]): Writer[A] => Writer[B] =
    identity[Writer[A]] _ >=> f

  def main(args: Array[String]): Unit = {
    println(
      fmap((x: Int) => x * 2).apply((5, "times 2"))
    )

    val a = 5
    val b = 3
    val c = 2
    println(
      bind((x: Int) => (c * x, s" * $c"))
        .apply(
          bind((x: Int) => pure(x + a))
            .apply((b, s"($a + $b)"))
        )
    )
  }
}
