package com.aidokay.scalaz.ch4

object jsEx {

  import json._
  import jsonInstances._

  object AccessResponse {
    implicit val json: JsDecoder[AccessResponse] = j => for {
      acc <- j.getAs[String]("access_token")
      tpe <- j.getAs[String]("token_type")
      exp <- j.getAs[Long]("expires_in")
      ref <- j.getAs[String]("refresh_token")
    } yield new AccessResponse(acc, tpe, exp, ref)
  }

  object RefreshResponse {
    implicit val json: JsDecoder[RefreshResponse] = j =>
      for {
        acc <- j.getAs[String]("access_token")
        tpe <- j.getAs[String]("token_type")
        exp <- j.getAs[Long]("expires_in")
      } yield new RefreshResponse(acc, tpe, exp)
  }

  def main(args: Array[String]): Unit = {
    /*val json = JsParser(
      s"""
         |{
         |"access_token": "BEARER_TOKEN",
         |"token_type": "Bearer",
         |"expires_in": 3600,
         |"refresh_token": "REFRESH_TOKEN"
         |}
         |""".stripMargin)*/
  }
}
