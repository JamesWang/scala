package com.aidokay.zio2

import zio.{IO, Schedule, Scope, ZIO, ZIOAppArgs, ZIOAppDefault, durationInt}

object ResilenceApp extends ZIOAppDefault {
  private sealed trait DownloadError extends Throwable

  private case object BandwidthExceeded extends DownloadError

  private case object NetworkError extends DownloadError

  private def download(id: String): IO[DownloadError, Array[Byte]] = ???

  private def isRecoverable(de: DownloadError): Boolean =
    de match
      case BandwidthExceeded => false
      case NetworkError => true

  private val policy = (Schedule.recurs(20) && Schedule.exponential(100.millis))
    .whileInput(isRecoverable)

  def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] = download("123").retry(policy)
}


