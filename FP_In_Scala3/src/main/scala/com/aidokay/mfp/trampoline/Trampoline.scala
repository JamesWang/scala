package com.aidokay.mfp.trampoline

import scala.annotation.tailrec

sealed trait Trampoline[+A]:
  @tailrec
  final def runT: A = resume match
    case Right(r) => r
    case Left(func) => func().runT

  @tailrec
  final def resume: Either[() => Trampoline[A], A] = this match
    case Done(v) => Right(v)
    case More(k) => Left(k)
    case FlatMap(sub, f) => sub match
      case Done(v) => f(v).resume
      case More(k) => Left(() => FlatMap(k(), f))
      case FlatMap(ssb, g) => (FlatMap(ssb, x => FlatMap(g(x), f)): Trampoline[A]).resume


/**
 * A step of the form Done(r) has a value v to return and
 * there are no more steps
 */
case class Done[A](value: A) extends Trampoline[A]

/**
 * A step of the form More(k), has more work to do, here k is a
 * closure which does some work and returns the next step
 */
case class More[+A](k: () => Trampoline[A]) extends Trampoline[A]

case class FlatMap[A, +B](sub: Trampoline[A], k: A => Trampoline[B]) extends Trampoline[B]