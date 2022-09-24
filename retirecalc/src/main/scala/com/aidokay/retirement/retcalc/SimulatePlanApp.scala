package com.aidokay.retirement.retcalc

import com.aidokay.retirement.csv.Decoder.Row
import com.aidokay.retirement.retcalc.Models.{EquityData, InflationData, RetCalcParams, RowExtender}

import scala.deriving.Mirror.ProductOf

object SimulatePlanApp extends App, RowExtender {

  def strMain(args: Array[String]): String =
    val (from +: until +: Nil) = args(0).split(",").toList
    val nbOfYearsSaving = args(1).toInt
    val nbOfYearsRetired = args(2).toInt

    val allReturns = Returns.fromEquityAndInflationData(
      equities = EquityData.fromResource("sp500.tsv"),
      inflations = InflationData.fromResource("cpi.tsv")
    )
    val params: Row = args.toList.drop(1)

    val p = params.as[RetCalcParams]
    println(p)
    RetCalc.simulatePlan(
      returns = allReturns.fromUntil(from, until),
      params = RetCalcParams(
        nbOfMonthsInRetirement = nbOfYearsRetired * 12,
        netIncome = args(3).toInt,
        currentExpenses = args(4).toInt,
        initialCapital = args(5).toInt
      ),
      nbOfMonthsSavings = nbOfYearsSaving * 12
    ) match
      case Right((cAtRetire, cAfterDeath)) =>
        s"""
           |Capital after $nbOfYearsSaving years of savings:
           |${cAtRetire.round}
           |Capial after $nbOfYearsRetired years in retirement:
           |${cAfterDeath.round}
        """.stripMargin
      case Left(error) => error.message

}
