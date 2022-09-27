package com.aidokay.contract.domain.invoice

import com.aidokay.contract.domain.{Lookup, PDFParser}
import com.aidokay.contract.domain.invoice.InvoiceField._
import com.aidokay.contract.domain.model.{Invoice, PdfValue}
import com.aidokay.contract.domain.ValueGetter.SplitDate

object InvoiceParser extends PDFParser[InvoiceField.Value, Invoice] {
  implicit def valueToPaymentLookup(x: Value): Lookup[Value] = x.asInstanceOf[InvoiceLookup[Value]]
  def sourceValues(): Map[InvoiceField.Value, Int] =
    InvoiceField.values.toSeq.map(lp ⇒ lp → lp.getIndex).toMap

  def mapping(data: Map[String, PdfValue]): Invoice = {
    implicit val dd: Map[String, PdfValue] = data

    Invoice(
      TITLE.value,
      ADDRESS_1.value,
      ADDRESS_2.value,
      CONTRACTOR.value,
      PHONE.value,
      INVOICE_NUM.value,
      EMAIL.value,
      ASSIGNMENT.value,
      SUBTOTAL.value,
      HST.value,
      GST_NUM.value,
      TOTAL.value,
      TIME_PERIOD.value match {
        case SplitDate(start,end) ⇒ (start, end)
      }
    )
  }
}
