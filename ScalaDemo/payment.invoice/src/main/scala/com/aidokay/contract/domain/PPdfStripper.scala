package com.aidokay.contract.domain

import java.util.concurrent.atomic.AtomicInteger

import org.apache.pdfbox.text.PDFTextStripper

import scala.collection.mutable.ListBuffer


class PPdfStripper extends PDFTextStripper {
    val pdfTextRowMap: ListBuffer[Map[String,String]] = ListBuffer[Map[String,String]]()

    val uid = new AtomicInteger(0)

    def extractValue(values: Option[String]): (String,String) = {
      values match {
        case Some(v) ⇒
          val tmp = v.split(":")
          if (tmp.nonEmpty && tmp(0) != null && !tmp(0).equals("")) {
            if (tmp.length > 1) {
              (tmp(0), tmp(1))
            } else {
              ("UID_" + uid.getAndIncrement(), tmp(0))
            }
          } else {
            ("","")
          }
      }
    }
}
