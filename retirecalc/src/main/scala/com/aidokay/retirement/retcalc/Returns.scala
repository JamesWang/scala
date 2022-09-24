package com.aidokay.retirement.retcalc


import com.aidokay.retirement.retcalc.Models.{EquityData, InflationData, Result, RetCalcError}

import scala.annotation.tailrec

sealed trait Returns

case class FixedReturns(annualRate: Double) extends Returns
case class VariableReturns(returns: Vector[VariableReturn]) extends Returns {
  def fromUntil(monthIdFrom: String, monthIdUntil: String): VariableReturns =
    VariableReturns(
      returns
        .dropWhile(_.monthId != monthIdFrom)
        .takeWhile(_.monthId != monthIdUntil)
    )
}
case class OffsetReturns(orig: Returns, offset: Int) extends Returns
case class VariableReturn(monthId: String, monthlyRate: Double)

object Returns {
  @tailrec
  def monthlyRate(returns: Returns, month: Int): Result[Double] = returns match
    case FixedReturns(r) => Right(r / 12)
    case VariableReturns(rates) =>
      if(rates.isDefinedAt(month))
        Right(rates(month % rates.length).monthlyRate)
      else
        Left(RetCalcError.ReturnMonthOutOfBounds(month, rates.size -1))
    case OffsetReturns(rs, offset) => monthlyRate(rs, month + offset)

  def fromEquityAndInflationData(equities: Vector[EquityData], inflations: Vector[InflationData]) : VariableReturns =
    VariableReturns(
      equities.zip(inflations).sliding(2).collect {
        case (preEquity, prevInflation) +: (equity, inflation) +: Vector() =>
          val inflationRate = inflation.value / prevInflation.value
          val totalReturn = (equity.value + equity.monthlyDividend) / preEquity.value
          val realTotalReturn = totalReturn - inflationRate
          VariableReturn(equity.monthId, realTotalReturn)
      }.toVector
    )
}
