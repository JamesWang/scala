package com.aidokay.contract.domain

import com.aidokay.contract.domain.invoice.InvoiceField.{datePeriod, datePeriodSplit}
import com.aidokay.contract.domain.model.PdfValue

trait ValueGetter[A] {
  def getValue(lookup: Lookup[A], data: Map[String, PdfValue]): A
}

object ValueGetter {
  def apply[A: ValueGetter]: ValueGetter[A] = implicitly

  implicit object stringValue extends ValueGetter[String] {
    override def getValue(lookup: Lookup[String], data: Map[String, PdfValue]): String = {
      data.getOrElse(lookup.getLabel, PdfValue(lookup.getLabel, "Not found")).value
    }
  }

  implicit object doubleValue extends ValueGetter[Double] {
    override def getValue(lookup: Lookup[Double], data: Map[String, PdfValue]): Double = {
      data(lookup.getLabel).value.toDouble
    }
  }

  implicit object decimalValue extends ValueGetter[BigDecimal] {
    override def getValue(lookup: Lookup[BigDecimal], data: Map[String, PdfValue]): BigDecimal = {
      BigDecimal(data(lookup.getLabel)
        .value
        .trim
        .stripPrefix("$").stripSuffix("$")
        .replace(",", "")
      )
    }
  }

  implicit object TupleValueStr extends ValueGetter[(String, String)] {
    override def getValue(lookup: Lookup[(String, String)], data: Map[String, PdfValue]): (String, String) = {
      datePeriod(data(lookup.getLabel).value)
    }
  }

  case class SplitDate(start: String, end: String)

  implicit object strValueTuple2 extends ValueGetter[SplitDate] {
    override def getValue(lookup: Lookup[SplitDate], data: Map[String, PdfValue]): SplitDate = {
      datePeriodSplit(data(lookup.getLabel).value)
    }
  }

}
