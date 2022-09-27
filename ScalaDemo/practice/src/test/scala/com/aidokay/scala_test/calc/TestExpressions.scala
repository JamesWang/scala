package com.aidokay.scala_test.calc

import com.aidokay.calc.ComplexCalculator.{Addition, Division, Failure, Number, SquareRoot, Subtract, Success}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper

class TestExpressions extends AnyFlatSpec with Matchers {

  "1 + 2" should "have value 3" in {
    Addition(Number(1), Number(2)).eval should be(Success(3))
  }

  "SquareRoot(-1)" should "have Failure(Square root of negative number" in {
    SquareRoot(Number(-1)).eval should be(Failure("Square root of negative number"))
  }

  "Division 8 by (6-2)" should "have value Success(2.0)" in {
    Division(
      Addition(
        Subtract(Number(8), Number(6)),
        Number(2)
      ),
      Number(2)
    ).eval should be(Success(2.0))
  }
}
