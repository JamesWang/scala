package com.aidokay.contract.domain.payment

import com.aidokay.contract.domain.{Lookup, ParseField}

object PaymentField extends ParseField {

  case class PaymentLookup[T](private val lbl: String, private val idx: Int, private val rge: (Int, Int))
    extends super.Val with Lookup[T] {
    override val getLabel: String = lbl
    override val getIndex: Int = idx
    override val getRange: (Int, Int) = rge
  }


  val TITLE:       Lookup[String] = PaymentLookup[String]("Title",        1, (1, -1))
  val DATE:        Lookup[String] = PaymentLookup[String]("Date",         3, (12, -1))
  val VENDOR_NUM:  Lookup[String] = PaymentLookup[String]("Vendor #",     6, (0, 10))
  val VENDOR:      Lookup[String] = PaymentLookup[String]("Vendor",      54, (0, 15))
  val CONTRACTOR:  Lookup[String] = PaymentLookup[String]("Contractor",  54, (16, -1))
  val WEEK_BILLED: Lookup[String] = PaymentLookup[String]("Week Billed", 56, (0, 10))

  val WEEK_WORKED: Lookup[(String, String)] = PaymentLookup[(String, String)]("Week Worked", 56, (11, 21))

  val HOURS:       Lookup[Double]     = PaymentLookup[Double]("Hours",          56, (37, 44))
  val RATE:        Lookup[BigDecimal] = PaymentLookup[BigDecimal]("Rate",       56, (45, 50))
  val AMOUNT:      Lookup[BigDecimal] = PaymentLookup[BigDecimal]("Amount",     56, (57, 68))
  val TAX_AMT:     Lookup[BigDecimal] = PaymentLookup[BigDecimal]("Tax Amount", 57, (35, -1))
  val CURRENCY:    Lookup[String]     = PaymentLookup[String]("Currency",       76, (0, 3))
  val TOTAL:       Lookup[BigDecimal] = PaymentLookup[BigDecimal]("Invoice Total", 76, (4, -1))

}
