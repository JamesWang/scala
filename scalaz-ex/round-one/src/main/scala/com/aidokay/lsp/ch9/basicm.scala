package com.aidokay.lsp.ch9

import scala.annotation.tailrec
import scala.util.{Failure, Success, Try}

object basicm {
  trait Semigroup[S] {
    def op(s1: S, s2: S): S
  }

  trait Monoid[S] extends Semigroup[S] {
    def identity: S
  }

  trait Functor[F[_]] {
    def map[A, B](fa: F[A])(f: A => B): F[B]
  }

  trait Applicatie[F[_]] extends Functor[F] {
    def apply[A, B](fa: F[A])(ff: F[A => B]): F[B]
  }

  trait Monad[F[_]] extends Applicatie[F] {
    def unit[A](a: => A): F[A]

    def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B]

    def flattern[A](ffa: F[F[A]]): F[A] = flatMap(ffa)(identity) //fa => fa

    override def map[A, B](fa: F[A])(f: A => B): F[B] = flatMap(fa)(a => unit(f(a)))

    override def apply[A, B](fa: F[A])(ff: F[A => B]): F[B] = flatMap(ff)(f => map(fa)(f))
  }


  implicit val optionMonad: Monad[Option] = new Monad[Option] {

    override def unit[A](a: => A): Option[A] = Some(a)

    override def flatMap[A, B](fa: Option[A])(f: A => Option[B]): Option[B] =
      fa match {
        case Some(x) => f(x)
        case _ => None
      }
  }

  implicit def eitherMonad[L]: Monad[Either[L, _]] = new Monad[Either[L, _]] {

    override def unit[A](a: => A): Either[L, A] = Right(a)

    override def flatMap[A, B](fa: Either[L, A])(f: A => Either[L, B]): Either[L, B] =
      fa match {
        case Right(r) => f(r)
        case Left(l) => Left(l)
      }
  }

  type UnitEither[R] = Either[Unit, R]

  implicit val listMonad: Monad[List] = new Monad[List] {

    override def unit[A](a: => A): List[A] = List(a)

    override def flatMap[A, B](fa: List[A])(f: A => List[B]): List[B] = {
      @tailrec
      def doFlatMap(acc: List[B], as: List[A]): List[B] = as match {
        case Nil => Nil
        case a :: aas => doFlatMap(f(a) ::: acc, aas)
      }

      doFlatMap(Nil, fa)
    }
  }

  type Id[A] = A

  implicit val idMonad = new Monad[Id] {

    override def unit[A](a: => A): Id[A] = a

    override def flatMap[A, B](fa: Id[A])(f: A => Id[B]): Id[B] = f(fa)
  }


  final case class State[S, A](run: S => (A, S)) {
    def compose[B](f: A => State[S, B]): State[S, B] = {
      State(s => {
        val (a, s1) = run(s)
        f(a).run(s1)
      })
    }
  }

  object State {
    def apply[S, A](a: => A): State[S, A] = new State[S, A](s => (a, s))

    def get[S]: State[S, S] = State(s => (s, s))

    def set[S](s: => S): State[S, Unit] = State(_ => ((), s))

  }

  implicit def stateMonad[S]: Monad[State[S, *]] = new Monad[State[S, *]] {

    override def unit[A](a: => A): State[S, A] = State(s => (a, s))

    override def flatMap[A, B](fa: State[S, A])(f: A => State[S, B]): State[S, B] = fa compose f

  }

  final case class Reader[R, A](run: R => A) {
    def compose[B](f: A => Reader[R, B]): Reader[R, B] = Reader(r => f(run(r)).run(r))
  }

  object Reader {
    def apply[R, A](a: => A): Reader[R, A] = new Reader[R, A](_ => a)
  }

  implicit def readerMonad[R]: Monad[Reader[R, *]] = new Monad[Reader[R, *]] {

    override def unit[A](a: => A): Reader[R, A] = Reader(a)

    override def flatMap[A, B](fa: Reader[R, A])(f: A => Reader[R, B]): Reader[R, B] =
      //Reader { r =>f(fa.run(r)).run(r)}
      fa compose f
  }

  final case class Writer[W: Monoid, A](run: (A, W)) {
    def compose[B](f: A => Writer[W, B]): Writer[W, B] = Writer {
      val (a, w) = run //first run
      val (b, w2) = f(a).run //second run
      val w3 = implicitly[Monoid[W]].op(w, w2) //then bind the writer info
      (b, w3) //then output
    }
  }

  object Writer {
    def apply[W: Monoid, A](a: => A): Writer[W, A] = new Writer((a, implicitly[Monoid[W]].identity))
  }

  implicit def writerMonad[W: Monoid]: Monad[Writer[W, *]] = new Monad[Writer[W, _]] {

    override def unit[A](a: => A): Writer[W, A] = Writer(a)

    override def flatMap[A, B](fa: Writer[W, A])(f: A => Writer[W, B]): Writer[W, B] = fa compose f
  }

  implicit def vectorMonoid[A] = new Monoid[Vector[A]] {

    override def identity: Vector[A] = Vector.empty[A]

    override def op(s1: Vector[A], s2: Vector[A]): Vector[A] = s1 ++ s2
  }

  implicit def tryMonad: Monad[Try[_]] = new Monad[Try[_]] {

    override def unit[A](a: => A): Try[A] = Success(a)

    override def flatMap[A, B](fa: Try[A])(f: A => Try[B]): Try[B] = fa match {
      case Success(a) => f(a)
      case Failure(exception) => Failure(exception)
    }
  }
}

