package com.aidokay.contract.domain.payment

import com.aidokay.contract.domain.model.Payment
import com.aidokay.contract.domain.JsonCreator._
import play.api.libs.json.Json

object PaymentPrinting {
  var totalTillNow: BigDecimal = 0

  def printPayment(payment: ((String, String), BigDecimal)): Unit = {
    val (start, end) = payment._1
    println(s"From $start to $end     $$${payment._2}")
    totalTillNow += payment._2
  }
  implicit class PrintPayment(payments: Seq[Payment]) {
    def printAll(): Unit = {
      val out = payments
        .map { payment ⇒
          val json = Json.prettyPrint(Json.toJson(payment))
          val json2 = Json.stringify(Json.toJson(payment))
          println(json)
          //println(paymentReads.reads(JsString(json2)))
          //print("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
          //println(Json.reads[Payment])
          //print("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
          (payment.weekWorked, payment.total)
        }.sortBy(_._1)

      println("-----------------------------------------------------------------------------------------")
      out.foreach {
        printPayment
      }
      val start = out.head._1._1
      val end = out.last._1._2
      println("-----------------------------------------------------------------------------------------")
      val totalWithoutHst = totalTillNow/1.13
      println(s"Total of $start ~ $end: $$$totalTillNow    without tax: $$$totalWithoutHst")
    }
  }
}
