package com.aidokay.scalaz.ch4

import com.aidokay.scalaz.ch4.json.{JsBoolean, JsDecoder, JsEncoder, JsInteger, JsString, JsValue}
import scalaz.\/

object jsonInstances {

  implicit class JsonValueOps(j: JsValue) {
    def getAs[A: JsDecoder](key: String): \/[String, A] = ???
  }

  implicit val stringJsonE: JsEncoder[String] = (obj: String) => JsString(obj)
  implicit val booleanJsonE: JsEncoder[Boolean] = (obj: Boolean) => JsBoolean(obj)
  implicit val integerJsonE: JsEncoder[Long] = (obj: Long) => JsInteger(obj)


  implicit val stringJsonD: JsDecoder[String] = (json: JsValue) => ???
  implicit val booleanJsonD: JsDecoder[Boolean] =(json: JsValue) => ???
  implicit val integerJsonD: JsDecoder[Long] = (json: JsValue) => ???


}