package com.aidokay.contract.domain.invoice

import com.aidokay.contract.domain.{Lookup, ParseField}
import com.aidokay.contract.domain.ValueGetter.SplitDate

object InvoiceField extends ParseField {

  case class InvoiceLookup[T](private val l: String, private val idx: Int, private val rge: (Int, Int)) extends super.Val with Lookup[T]{
    override val getLabel: String = l
    override val getIndex: Int = idx
    override val getRange: (Int, Int) = rge
  }

  val TITLE:       Lookup[String]     = InvoiceLookup[String]("Title",      0, (0, -1))
  val ADDRESS_1:   Lookup[String]     = InvoiceLookup[String]("Address 1",  1, (0, -1))
  val ADDRESS_2:   Lookup[String]     = InvoiceLookup[String]("Address 2",  2, (0, 22))
  val CONTRACTOR:  Lookup[String]     = InvoiceLookup[String]("Contractor", 2, (34, -1))
  val PHONE:       Lookup[String]     = InvoiceLookup[String]("Phone",      3, (6, 20))
  val INVOICE_NUM: Lookup[String]     = InvoiceLookup[String]("Invoice #",  3, (31, -1))
  val EMAIL:       Lookup[String]     = InvoiceLookup[String]("Email",      4, (8, 25))
  val ASSIGNMENT:  Lookup[String]     = InvoiceLookup[String]("Assignment", 5, (11, 20))

  val SUBTOTAL:    Lookup[BigDecimal] = InvoiceLookup[BigDecimal]("Subtotal",   20, (10, -1))
  val HST:         Lookup[BigDecimal] = InvoiceLookup[BigDecimal]("HST",        21, (10, -1))
  val GST_NUM:     Lookup[String]     = InvoiceLookup[String]("GST #",          23, (7, 16))
  val TOTAL:       Lookup[BigDecimal] = InvoiceLookup[BigDecimal]("TOTAL",      23, (23, -1))
  val TIME_PERIOD: Lookup[SplitDate]  = InvoiceLookup[SplitDate]("Time Period", 24, (14, -1))
}
