package com.aidokay.cats3.fiber

import cats.effect.{ExitCode, IO, IOApp, Ref}
import com.aidokay.cats3.fiber.ProducerConsumer.{consumer, producer}
import cats.syntax.all._
import cats.effect.std.Console
import scala.collection.immutable.Queue

object ProduceConsumeApp extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = ???
   /* for {
      queueR <- Ref.of[IO, Queue[Int]](Queue.empty[Int])
      res    <- (consumer(queueR), producer(queueR, 0))
        .parMapN((_, _) => ExitCode.Success)
        .handleErrorWith{ t =>
          Console[IO].println(s"Error caught: ${t.getMessage}").as(ExitCode.Error)
        }
    } yield res*/
}
