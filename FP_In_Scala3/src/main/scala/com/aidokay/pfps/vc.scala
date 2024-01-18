package com.aidokay.pfps

object vc {
  case class Username(val value: String) extends AnyVal

  case class Email(val value: String) extends AnyVal

  //def mkUsername(value: String): Option[Username]  = (value.nonEmpty).guard[Option].as(Username(value))
}
