package com.aidokay.scala_test.flat

import com.aidokay.scala_test.Calculator.addTwo
import org.scalatest.flatspec.AnyFlatSpecLike
import com.aidokay.scala_test.CalculatorInstances._

class AddSpec extends AnyFlatSpecLike {
  "addition of 3 and 3 " should "have result 6" in {
    assert(addTwo(3,3) == 6)
  }

  "addition of 5.1 and 3.2 " should "have result 8.3" in {
    assert(addTwo(5.1,3.2) == 8.3)
  }

  "addition of BigDecimal 5.1 and 3.2 " should "have result 8.3" in {
    assert(addTwo(BigDecimal("5.1"),BigDecimal(3.2)) == BigDecimal(8.3))
  }
}
