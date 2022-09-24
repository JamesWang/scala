package com.aidokay.retirement.retcalc

import com.aidokay.retirement.retcalc.Models.RetCalcError.MoreExpensesThanIncome
import com.aidokay.retirement.retcalc.Models.{Result, RetCalcError, RetCalcParams}

import scala.annotation.tailrec

object RetCalc {
  def futureCapital(
                     returns: Returns,
                     nbOfMonths: Int,
                     netIncome: Int,
                     currentExpense: Int,
                     initialCapital: Double): Result[Double] = {

    val monthlySavings = netIncome - currentExpense
    (0 until nbOfMonths).foldLeft[Result[Double]](Right(initialCapital)) {
      case (acc, month) =>
        for {
          ac <- acc
          monthlyRate <- Returns.monthlyRate(returns, month)
        } yield ac * (1 + monthlyRate) + monthlySavings

    }
  }

  def simulatePlan(returns: Returns, nbOfMonthsSavings: Int, params: RetCalcParams): Result[(Double, Double)] =
    for {
      capitalAtRetirement <- futureCapital(
        returns = returns,
        nbOfMonths = nbOfMonthsSavings,
        netIncome = params.netIncome,
        currentExpense = params.currentExpenses,
        initialCapital = params.initialCapital
      )
      capitalAfterDeath <- futureCapital(
        returns = returns,
        nbOfMonths = params.nbOfMonthsInRetirement,
        netIncome = 0,
        currentExpense = params.currentExpenses,
        initialCapital = capitalAtRetirement
      )
    } yield (capitalAtRetirement, capitalAfterDeath)

  def nbOfMonthsSaving(params: RetCalcParams, returns: Returns): Result[Int] =
    @tailrec
    def loop(months: Int): Result[Int] =
      simulatePlan(
        returns = returns,
        params = params,
        nbOfMonthsSavings = months,
      ) match {
        case Right(_, cAfterDeath) =>
          if (cAfterDeath > 0.0)
            Right(months)
          else loop(months + 1)
        case Left(err) => Left(err)
      }

    if (params.netIncome > params.currentExpenses)
      loop(0)
    else
    //Never can save enough
      Left(MoreExpensesThanIncome(params.netIncome, params.currentExpenses))
}
