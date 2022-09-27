package com.aidokay.contract.domain

import com.aidokay.contract.domain.model.{Convertible, Invoice, Payment}
import play.api.libs.functional.syntax.{toFunctionalBuilderOps, unlift}
import play.api.libs.json._

object JsonCreator {
  implicit val formats: Format[Payment] = Json.format[Payment]
  implicit val formats2: Format[Invoice] = Json.format[Invoice]

  @inline def write[T](label: String)(implicit x: Writes[T]): OWrites[T] = {
    (JsPath \ label).write[T]
  }


  implicit def paymentWrites: Writes[Payment] = (
    write[String]("title") and
      write[String]("date") and
      write[String]("vendor #") and
      write[String]("vendor") and
      write[String]("contractor") and
      write[String]("weekBilled") and
      write[(String, String)]("weekWorked") and
      write[Double]("hours") and
      write[BigDecimal]("rate") and
      write[BigDecimal]("amount") and
      write[BigDecimal]("tax") and
      write[String]("currency") and
      write[BigDecimal]("total")
    ) (unlift(Payment.unapply))

  implicit def invoiceWrites: Writes[Invoice] = (
      write[String]("title") and
      write[String]("address_1") and
      write[String]("address_2") and
      write[String]("contractor") and
      write[String]("phone") and
      write[String]("invoice_num") and
      write[String]("email") and
      write[String]("assignment") and
      write[BigDecimal]("subtotal") and
      write[BigDecimal]("hst") and
      write[String]("gst") and
      write[BigDecimal]("total") and
      write[(String,String)]("time_period")
    ) (unlift(Invoice.unapply))

  trait PrintAsJson[T <: Convertible] {
    def print(t: Seq[T])(implicit writes: Writes[T]): Unit = {
      t.foreach { x ⇒
        println(Json.prettyPrint(Json.toJson(x)))
      }
    }
  }

  trait ConvertibleToJson[T] {
    def toJson: String
  }

  object ConvertibleToJson {
    def apply[T:ConvertibleToJson]:ConvertibleToJson[T] = implicitly
  }

  implicit class InvoiceToJson(data: Invoice) extends ConvertibleToJson[Invoice] {
    def toJson: String = Json.prettyPrint(Json.toJson(data))
  }

  implicit class PaymentToJson(data: Payment) extends ConvertibleToJson[Payment]{
    def toJson: String = Json.prettyPrint(Json.toJson(data))
  }

  implicit class PrintPayment(data: Seq[Payment]) extends PrintAsJson[Payment]{
    def printJson(): Unit = super.print(data)
  }
  implicit class PrintInvoice(data: Seq[Invoice]) extends PrintAsJson[Invoice] {
    def printJson(): Unit = super.print(data)
  }
}

