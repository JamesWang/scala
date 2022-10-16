package com.aidokay.contract.domain

import com.aidokay.contract.domain.invoice.InvoiceField.{datePeriod, datePeriodSplit}
import com.aidokay.contract.domain.model.PdfValue

trait ValueGetter[A] {
  def getValue(lookup: Lookup[A], data: Map[String, PdfValue]): Option[A]
  def identity: A
}

object ValueGetter {
  def apply[A: ValueGetter]: ValueGetter[A] = implicitly

  implicit object stringValue extends ValueGetter[String] {
    override def getValue(lookup: Lookup[String], data: Map[String, PdfValue]): Option[String] = {
      data.get(lookup.getLabel).map(_.value)
    }

    override def identity: String = ""
  }

  implicit object doubleValue extends ValueGetter[Double] {
    override def getValue(lookup: Lookup[Double], data: Map[String, PdfValue]): Option[Double] = {
      data.get(lookup.getLabel).map(_.value.toDouble)
    }

    override def identity: Double = 0.0d
  }

  implicit object decimalValue extends ValueGetter[BigDecimal] {
    override def getValue(lookup: Lookup[BigDecimal], data: Map[String, PdfValue]): Option[BigDecimal] = {
      data.get(lookup.getLabel)
        .map(_.value.trim.stripPrefix("$").stripSuffix("$").replace(",", ""))
        .map(BigDecimal(_))
    }

    override def identity: BigDecimal = BigDecimal(0.0)
  }

  implicit object TupleValueStr extends ValueGetter[(String, String)] {
    override def getValue(lookup: Lookup[(String, String)], data: Map[String, PdfValue]): Option[(String, String)] = {
      data.get(lookup.getLabel).map(_.value).map(datePeriod)
    }

    override def identity: (String, String) = ("","")
  }

  case class SplitDate(start: String, end: String)

  implicit object strValueTuple2 extends ValueGetter[SplitDate] {
    override def getValue(lookup: Lookup[SplitDate], data: Map[String, PdfValue]): Option[SplitDate] = {
      data.get(lookup.getLabel).map(_.value).map(datePeriodSplit)
    }

    override def identity: SplitDate = SplitDate("","")
  }

}
