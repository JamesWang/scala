package com.aidokay.contract.domain

import java.time.format.DateTimeFormatter
import java.time.LocalDate
import java.time.temporal.ChronoUnit

import com.aidokay.contract.domain.ValueGetter.SplitDate


abstract class ParseField extends Enumeration {

  def datePeriod(date: String): (String, String) = {
    val dateFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy")
    val localDate = LocalDate.from(dateFormatter.parse(date))
    val weekAgo = localDate.minus(7, ChronoUnit.DAYS)
    val startDay = dateFormatter.format(weekAgo)
    (startDay, date)
  }

  def datePeriodSplit(date: String): SplitDate = {
    date.split("to").toSeq.map(_.trim) match {
      case Seq(start, end) ⇒ SplitDate(start, end)
    }
  }
}

