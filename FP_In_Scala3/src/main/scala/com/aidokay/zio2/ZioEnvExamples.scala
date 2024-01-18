package com.aidokay.zio2

import zio._

object ZioEnvExamples {

  private trait LoggingService:
    def log(line: String): Unit

  private trait EmailService:
    def send(user: String, content: String): UIO[Unit]

  private object LoggingService:
    def log(line: String): ZIO[LoggingService, Nothing, Unit] =
      //notice here is ZIO.serviceWith(), not ZIO.serviceWithZIO() if trait signature is not returning ZIO[R, E, A]
      ZIO.serviceWith[LoggingService](_.log(line))

  private object EmailService:
    def send(user: String, content: String): ZIO[EmailService, Nothing, Unit] =
      //notice here is ZIO.serviceWithZIO(), since trait signature is returning ZIO[R, E, A]
      ZIO.serviceWithZIO[EmailService](_.send(user, content))

  private case class LoggingServiceLive() extends LoggingService:
    override def log(line: String): Unit = println(line)

  private case class EmailServiceFake() extends EmailService:
    override def send(user: String, content: String): UIO[Unit] =
      ZIO.succeed({
        println(s"sending email to $user")
        println(s"Content: $content}")
      })

  private val testableRuntime = Runtime.default.withEnvironment(
    ZEnvironment[LoggingService, EmailService](LoggingServiceLive(), EmailServiceFake())
  )

  def main(args: Array[String]): Unit =
    Unsafe.unsafe { implicit unsafe =>
      testableRuntime.unsafe.run(
        for {
          _ <- LoggingService.log("sending newsletter")
          _ <- EmailService.send("David", "Hi!, Here is today's newsletter.")
        } yield ()
      ).getOrThrowFiberFailure()
    }
}
