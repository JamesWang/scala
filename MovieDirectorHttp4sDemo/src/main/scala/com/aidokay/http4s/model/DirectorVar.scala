package com.aidokay.http4s.model

import scala.util.Try

object DirectorVar {

  def unapply(str: String): Option[Director] =
    if (str.nonEmpty && str.matches(".*.*")) {
      Try {
        val splitted = str.split(' ')
        Director(splitted(0), splitted(1))
      }.toOption
    } else None

}
