package com.aidokay.contract.domain

trait Converter[-A] {
  def convert(x: A): Seq[_]
}

object Converter {
  def apply[A: Converter]: Converter[A] = implicitly
}
