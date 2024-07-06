package com.aidokay.lsp

import com.aidokay.lsp.ch9.basicm.Functor


package object ch10 {
  /**
   *
   */
  class Free[F[_] : Functor, A] {
    def flatMap[B](f: A => Free[F, B]): Free[F, B] = this match {
      case Done(a) => f(a)
      case Join(a) => Join(implicitly[Functor[F]].map(a)(_.flatMap(f)))
    }

    def map[B](f: A => B): Free[F, B] = flatMap(a => Done(f(a)))
  }

  case class Bait(name: String) extends AnyVal

  case class Line(length: Int) extends AnyVal

  case class Fish(name: String) extends AnyVal

  sealed trait Action[A]

  final case class BuyBait[A](name: String, f: Bait => A) extends Action[A]

  final case class CastLine[A](bait: Bait, f: Line => A) extends Action[A]

  final case class HookFish[A](line: Line, f: Fish => A) extends Action[A]

  //monadic
  final case class Done[F[_] : Functor, A](a: A) extends Free[F, A]

  final case class Join[F[_] : Functor, A](action: F[Free[F, A]]) extends Free[F, A]


  implicit val actionFunctor: Functor[Action] = new Functor[Action] {
    override def map[A, B](fa: Action[A])(f: A => B): Action[B] = fa match {
      case BuyBait(name, g) => BuyBait(name, x => f(g(x)))
      case CastLine(bait, g) => CastLine(bait, x => f(g(x)))
      case HookFish(line, g) => HookFish(line, x => f(g(x)))
    }
  }
}
