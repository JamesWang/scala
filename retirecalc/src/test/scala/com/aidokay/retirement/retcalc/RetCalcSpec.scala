package com.aidokay.retirement.retcalc

import com.aidokay.retirement.retcalc.Models.{RetCalcError, RetCalcParams}
import org.scalactic.{Equality, TolerantNumerics, TypeCheckedTripleEquals}
import org.scalatest.EitherValues
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class RetCalcSpec extends AnyWordSpec, Matchers, TypeCheckedTripleEquals, EitherValues  {
  given doubleEquality: Equality[Double] = TolerantNumerics.tolerantDoubleEquality(0.0001)

  "RetCalc.futureCapital" should {
    "calculate the amount of savings I will have in n months" in {
      val actual = RetCalc.futureCapital(
        returns = FixedReturns(0.04),
        nbOfMonths = 25 * 12,
        netIncome = 3000,
        currentExpense = 2000,
        initialCapital = 10000
      ).value
      val expected = 541267.1990
      actual should ===(expected)
    }
  }

  val params: RetCalcParams = RetCalcParams(
    nbOfMonthsInRetirement = 40 * 12,
    netIncome = 3000,
    currentExpenses = 2000,
    initialCapital = 10_000
  )
  "RetCalc.simulatePlan" should {
    "calculate the capital at retirement and the capital after death" in {
      val (cAtRetirement: Double, cAfterDeath: Double) = RetCalc.simulatePlan(
        returns = FixedReturns(0.04),
        params,
        nbOfMonthsSavings = 25 * 12,
      ).value
      cAtRetirement should ===(541267.1990)
      cAfterDeath should ===(309867.5316)
    }
  }

  "RetCalc.nbOfMonthsSaving" should {
    "calculate how long I need to save before I can retire" in {
      val actual = RetCalc.nbOfMonthsSaving(
        params = RetCalcParams(
          nbOfMonthsInRetirement = 40 * 12,
          netIncome = 3000,
          currentExpenses = 2000,
          initialCapital = 10_000
        ),
        returns = FixedReturns(0.04)
      ).value
      val expected = 23 * 12 + 1
      actual should ===(expected)
    }
    "not loop forever if I enter bad parameters" in {
      val actual = RetCalc.nbOfMonthsSaving(params.copy(netIncome = 1000), FixedReturns(0.04)).left.value
      actual should ===(RetCalcError.MoreExpensesThanIncome(1000, 2000))
    }
  }
}
