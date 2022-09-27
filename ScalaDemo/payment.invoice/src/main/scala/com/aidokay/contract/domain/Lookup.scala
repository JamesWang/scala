package com.aidokay.contract.domain

import com.aidokay.contract.domain.model.PdfValue

trait Lookup[T] {
  def value1(implicit data: Map[String, PdfValue], vg: ValueGetter[T]): T = vg.getValue(this, data)
  def value[A <: T : ValueGetter](implicit data: Map[String, PdfValue]): A =
    ValueGetter[A].getValue(this.asInstanceOf[Lookup[A]], data)
  def getLabel: String
  def getIndex: Int
  def getRange: (Int, Int)
}