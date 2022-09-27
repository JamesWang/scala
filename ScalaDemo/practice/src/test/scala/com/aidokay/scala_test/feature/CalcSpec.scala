package com.aidokay.scala_test.feature

import com.aidokay.scala_test.Calculator.addTwo
import com.aidokay.scala_test.CalculatorInstances.bigDecimalCalculator
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec

class CalcSpec extends AnyFeatureSpec with GivenWhenThen{
  info("As a calculator owner")
  info("I want to be able to add two numbers")
  info("So I can get correct result")
  Feature("Addition") {
    Scenario("User adds two numbers"){
      Given("A calculator")
      import com.aidokay.scala_test.CalculatorInstances.intCalculator
      When("two integers are added")
      val result = addTwo(3,3)
      Then("we get correct result")
      assert(result == 6)

      When("two BigDecimals are added")
      val result2 = addTwo(BigDecimal(100.122), BigDecimal(200.111))
      Then("we get correct result2")
      assert(result2 == BigDecimal(300.233))
    }
  }
}
