package com.aidokay.mfp.writer

trait Logging[F[_]] {
  def log(message: String): F[Unit]
}

object Logging:
  given writeLogging: Logging[SimpleWriter] = (message: String) => SimpleWriter.log(message)
