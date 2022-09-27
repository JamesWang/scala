package com.aidokay.calc

object JsonExample {

  val DOMAIN_DESIGN: String =
    """
      |Json::=
      | JsString (value: String)
      | JsNumber (value: Double)
      | JsBoolean(value: Boolean)
      | JsNull
      | JsSequence
      | JsObject
      |JsSequence ::= SeqCell( head: Json, tail: JsSequence)
      | SeqEnd
      |JsObject ::= ObjectCell(key: String, value: Json, tail: JsObject
      | ObjectEnd
      |""".stripMargin


  sealed trait Json
  final case class JsString(v: String) extends Json
  final case class JsNumber(v: Double) extends Json
  final case class JsBoolean(v: Boolean) extends Json
  final case object JsNull extends Json

  sealed trait JsSequence extends Json
  final case class SeqCell(head: Json, tail: JsSequence) extends JsSequence
  case object SeqEnd extends JsSequence

  sealed trait JsObject extends Json
  final case class ObjectCell(key: String, value: Json, tail: JsObject) extends JsObject
  case object ObjectEnd extends JsObject

}
