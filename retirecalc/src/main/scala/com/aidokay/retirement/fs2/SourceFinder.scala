package com.aidokay.retirement.fs2

import cats.effect.IO
import fs2.{Stream, text}
import fs2.io.file.{Files, Path}

object SourceFinder {

  case class VarInApp(repo: String, varName: String, defaultValue: Option[String])

  private def repoName(filename: String) =
    filename.substring(filename.lastIndexOf("\\") + 1, filename.indexOf("."))

  def load(filename: String): Stream[IO, VarInApp] =
    val repo = repoName(filename)
    Files[IO].readAll(Path(filename))
      .through(text.utf8.decode)
      .through(text.lines)
      .map(_.trim)
      .withFilter(s => s.nonEmpty && !s.startsWith("#"))
      .withFilter(_.contains("$"))
      .map(_.split("\\$")(1))
      .map(_.replace("{", "").replace("}", ""))
      .map { e =>
        val arr = e.split(":")
        VarInApp(repo, arr(0), if (arr.length > 1) Option(arr(1)) else None)
      }


}
