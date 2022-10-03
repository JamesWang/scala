package com.aidokay.cats3.copy

import java.io.{Closeable, File, FileInputStream, FileOutputStream, InputStream, OutputStream}
import cats.effect.{IO, IOApp, Resource, Sync}
import cats.syntax.all.*
import com.sun.management.VMOption.Origin
import cats.effect.unsafe.implicits.global
object FileCopy {

  trait IOStream[I, O] {
    def in(f: I): InputStream
    def out(f: O): OutputStream
  }

  def transmit[F[_] : Sync](origin: InputStream, dest: OutputStream, buffer: Array[Byte], acc: Long): F[Long] =
    for {
      amount <- Sync[F].blocking(origin.read(buffer, 0, buffer.length))
      count <- if (amount > -1) Sync[F].blocking(dest.write(buffer, 0, amount)) >> transmit(origin, dest, buffer, acc + amount)
      else Sync[F].pure(acc)
    } yield count

  def transfer[F[_] : Sync](origin: InputStream, dest: OutputStream): F[Long] =
    transmit(origin, dest, new Array[Byte](102 * 10), 0)

  def ioStream[F[_] : Sync, S <: Closeable](s: => S): Resource[F, S] =
    Resource.make(Sync[F].pure(s))(
      r => Sync[F].blocking(r.close()).handleErrorWith(_ => Sync[F].unit)
    )

  def inputOutputStream[F[_] : Sync](in: File, out: File)(using io: IOStream[File, File]): Resource[F, (InputStream, OutputStream)] =
    for {
      i <- ioStream(io.in(in))
      o <- ioStream(io.out(out))
    } yield (i, o)

  object FileCopier {

    given fioStream: IOStream[File, File] with
      def in(f: File) = new FileInputStream(f)
      def out(f: File) = new FileOutputStream(f)

    def copy[F[_] : Sync](origin: File, dest: File): F[Long] = inputOutputStream(origin, dest).use(transfer)
  }
}
