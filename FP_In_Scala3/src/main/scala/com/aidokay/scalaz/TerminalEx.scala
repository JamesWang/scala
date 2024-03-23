package com.aidokay.scalaz

import scala.concurrent.Future

object TerminalEx {
  trait Terminal[C[_]] {
    def read: C[String]

    def write(t: String): C[Unit]
  }

  type Now[X] = X

  trait TerminalSync extends Terminal[Now] {
    def read: String = ???

    def write(t: String): Unit
  }

  trait TerminalAsync extends Terminal[Future] {
    def read: Future[String] = ???

    def write(t: String): Future[Unit] = ???
  }

  trait Execution[C[_]] {
    def chain[A, B](c: C[A])(f: A => C[B]): C[B]

    def create[B](b: B): C[B]
  }

  def echoV1[C[_]](t: Terminal[C], e: Execution[C]): C[String] =
    e.chain(t.read) { in =>
      e.chain(t.write(in)) { _ =>
        e.create(in)
      }
    }

  private object Execution {
    extension [A, C[_]](c: C[A]) {
      def flatMap[B](f: A => C[B])(using e: Execution[C]): C[B] = e.chain(c)(f)
      def map[B](f: A => B)(using e: Execution[C]): C[B] =
        e.chain(c)(f andThen e.create)
    }
  }
  import Execution.*
  def echoV2[C[_]](using t: Terminal[C], e: Execution[C]): C[String] =
    for {
      in <- t.read
      _ <- t.write(in)
    } yield in

}
