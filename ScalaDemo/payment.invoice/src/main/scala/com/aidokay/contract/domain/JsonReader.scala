package com.aidokay.contract.domain

import com.aidokay.contract.domain.model.{Invoice, Payment}
import play.api.libs.functional.syntax.toFunctionalBuilderOps
import play.api.libs.json.{JsPath, Reads}

object JsonReader {
 // implicit val formats: Format[Payment] = Json.format[Payment]
 // implicit val formats2: Format[Invoice] = Json.format[Invoice]

  @inline def read[T](label: String)(implicit x: Reads[T]): Reads[T] = {
    (JsPath \ label).read[T]
  }


  implicit def paymentReads: Reads[Payment] = (
    (JsPath \ "title").read[String] and
      read[String]("date") and
      read[String]("vendor #") and
      read[String]("vendor") and
      read[String]("contractor") and
      read[String]("weekBilled") and
      read[(String, String)]("weekWorked") and
      read[Double]("hours") and
      read[BigDecimal]("rate") and
      read[BigDecimal]("amount") and
      read[BigDecimal]("tax") and
      read[String]("currency") and
      read[BigDecimal]("total")
    )(Payment.apply _)

  implicit def invoiceReads: Reads[Invoice] = (
    read[String]("title") and
      read[String]("address_1") and
      read[String]("address_2") and
      read[String]("contractor") and
      read[String]("phone") and
      read[String]("invoice_num") and
      read[String]("email") and
      read[String]("assignment") and
      read[BigDecimal]("subtotal") and
      read[BigDecimal]("hst") and
      read[String]("gst") and
      read[BigDecimal]("total") and
      read[(String,String)]("time_period")
    ) (Invoice.apply _)
}
