package com.aidokay.lsp.ch9

object basicm {

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
}
