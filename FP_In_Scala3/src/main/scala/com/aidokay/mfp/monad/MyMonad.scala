package com.aidokay.mfp.monad

object MyMonad {

  trait Monad[F[_]]:
    def pure[A](a: A): F[A]

    def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B]

    def map[A, B](fa: F[A])(f: A => B): F[B]


  extension[F[_] : Monad, A] (fa: F[A])
    def map[B](f: A => B): F[B] = summon[Monad[F]].map(fa)(f)
    def flatMap[B](f: A => F[B]): F[B] = summon[Monad[F]].flatMap(fa)(f)

}
