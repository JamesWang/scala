package com.aidokay.mfp.trampoline

import scala.annotation.tailrec

object TrampolineApp {

  sealed trait Trampoline[+A] {
    @tailrec
    final def runT: A = resume match {
      case Right(value) => value
      case Left(kk) => kk().runT
    }

    @tailrec
    final def resume: Either[() => Trampoline[A], A] = this match
      case Done(v) => Right(v)
      case More(k) => Left(() => k())
      case FlatMap(sub, f) => sub match
        case Done(vv) => f(vv).resume
        case More(kk) => Left(() => FlatMap(kk(), f))
        case FlatMap(ssb, g) => (FlatMap(ssb, x => FlatMap(g(x), f))).resume
  }

  case class Done[A](value: A) extends Trampoline[A]

  case class More[A](k: () => Trampoline[A]) extends Trampoline[A]

  case class FlatMap[A, B](sub: Trampoline[A], f: A => Trampoline[B]) extends Trampoline[B]
}
