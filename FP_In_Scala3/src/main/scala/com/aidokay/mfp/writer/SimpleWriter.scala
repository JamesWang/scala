package com.aidokay.mfp.writer

case class SimpleWriter[A](logs: List[String], value: A):
  def flatMap[B](f: A => SimpleWriter[B]): SimpleWriter[B] =
    val writerB: SimpleWriter[B] = f(value)
    SimpleWriter(logs ++ writerB.logs, writerB.value)

  def map[B](f: A => B): SimpleWriter[B] = SimpleWriter[B](logs, f(value))

  def map2[B](f: A => B): SimpleWriter[B] = flatMap(_ => SimpleWriter.pure(f(value)))


object SimpleWriter:
  def pure[A](value: A): SimpleWriter[A] = SimpleWriter(List(), value)

  def log(message: String): SimpleWriter[Unit] = SimpleWriter(List(message), ())