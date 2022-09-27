package com.aidokay.contract.domain.payment

import com.aidokay.contract.domain.{Lookup, PDFParser}
import com.aidokay.contract.domain.model.{Payment, PdfValue}
import com.aidokay.contract.domain.payment.PaymentField._

object PaymentParser extends PDFParser[PaymentField.Value, Payment] {
  implicit def valueToPaymentLookup(x: Value): Lookup[Value] = x.asInstanceOf[PaymentLookup[Value]]
  override def sourceValues(): Map[PaymentField.Value, Int] =
    PaymentField.values.toSeq.map(lp ⇒ lp → lp.getIndex).toMap
  override val skip = 1

  override def mapping(data: Map[String, PdfValue]): Payment = {
    implicit val dd: Map[String, PdfValue] = data

    Payment(
      TITLE.value,
      DATE.value,
      VENDOR_NUM.value,
      VENDOR.value,
      CONTRACTOR.value,
      WEEK_BILLED.value,
      WEEK_WORKED.value,
      HOURS.value,
      RATE.value,
      AMOUNT.value,
      TAX_AMT.value,
      CURRENCY.value,
      TOTAL.value
    )
  }
}
