package com.aidokay.contract.domain

package object model{
  trait Convertible

  case class PdfValue(label: String, value: String)

  case class BadData(msg: String)

}


