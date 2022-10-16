package com.aidokay.mfp

import scala.annotation.tailrec

object myLazyList {
  sealed trait MLazyStream[+A] //covariant A since it is used as return value

  case object Empty extends MLazyStream[Nothing]

  //explicit thunk
  case class Cons[+A](h: () => A, t: () => MLazyStream[A]) extends MLazyStream[A]

  trait StreamExtender:
    extension[A] (stream: MLazyStream[A])
      def headOption: Option[A] = stream match
        case Empty => None
        case Cons(h, _) => Option(h())

      def toList: List[A] =
        @tailrec
        def asList(acc: List[A], as: MLazyStream[A]): List[A] = as match
          case Empty => acc
          case Cons(h, tail) => asList(h() :: acc, tail())

        asList(Nil, stream).reverse

      def take(n: Int): MLazyStream[A] =
        def tk(m: Int, tail: MLazyStream[A]): MLazyStream[A] =
          if (m <= 0) Empty
          else {
            tail match
              case Empty => Empty
              case Cons(h, tl) => Cons(h, () => tk(m - 1, tl()))
          }

        tk(n, stream)

      def takeWhile(pf: A => Boolean): MLazyStream[A] =
        def doTakeWhile(remaing: MLazyStream[A]): MLazyStream[A] =
          remaing match
            case Empty => Empty
            case Cons(h, tail) if pf(h()) => Cons(h, () => doTakeWhile(tail()))
            case Cons(_, tail) => doTakeWhile(tail())

        doTakeWhile(stream)

      def drop(n: Int): MLazyStream[A] =
        @tailrec
        def doDrop(m: Int, next: MLazyStream[A]): MLazyStream[A] =
          if (m <= 0) next
          else next match
            case Empty => Empty
            case Cons(h, tail) =>
              println(s"dropping ${h()}, remaining: ${tail().toList}")
              doDrop(m - 1, tail())

        doDrop(n, stream)

  object MLazyStream:
    def cons[A](head: => A, tail: => MLazyStream[A]): MLazyStream[A] =
      lazy val hd = head
      lazy val tl = tail
      Cons(() => hd, () => tl)

    def empty[A]: MLazyStream[A] = Empty

    def apply[A](as: A*): MLazyStream[A] = as.toList match
      case Nil => empty
      case h :: tail => cons(h, apply(tail *))


}
