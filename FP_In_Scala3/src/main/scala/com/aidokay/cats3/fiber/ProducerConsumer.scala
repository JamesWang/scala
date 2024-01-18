package com.aidokay.cats3.fiber

import cats.data.State
import cats.effect.{Async, Deferred, Ref, Sync}
import cats.effect.std.Console
import cats.syntax.all.*

import scala.collection.immutable.Queue


object ProducerConsumer {

  case class State[F[_], A](queue: Queue[A], takers: Queue[Deferred[F, A]])

  def producer[F[_] : Sync : Console](id: Int, counterR: Ref[F, Int], stateR: Ref[F, State[F, Int]]): F[Unit] =
    def offer(i: Int): F[Unit] =
      stateR.modify {
        case State(queue, takers) if (takers.nonEmpty) =>
          val (taker, rest) = takers.dequeue
          State(queue, rest) -> taker.complete(i).void
        case State(queue, takers) => State(queue.enqueue(i), takers) -> Sync[F].unit
      }.flatten
    for {
      i <- counterR.getAndUpdate(_+1)
      _ <- offer(i)
      _ <- if (i % 10000 == 0 ) Console[F].println(s"Producer $id has reached $i items") else Sync[F].unit
      _ <- producer(id, counterR, stateR)
    } yield ()


  def consumer[F[_] : Async : Console](id: Int, stateR: Ref[F, State[F, Int]]): F[Unit] =
    val take : F[Int] =
      Deferred[F, Int].flatMap { taker =>
        stateR.modify {
          case State(queue, takers) if queue.nonEmpty =>
            val (i, rest) = queue.dequeue
            State(rest, takers) -> Async[F].pure(i)
          case State(queue, takers) =>
            State(queue, takers.enqueue(taker)) -> taker.get
        }.flatten
      }
    for {
      i <- take
      _ <- if (i % 10000 == 0 ) Console[F].println(s"Consumer $id has reached $i items") else Async[F].unit
      _ <- consumer(id, stateR)
    } yield ()
}

