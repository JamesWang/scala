package com.aidokay.algo

import org.scalactic.TripleEqualsSupport.TripleEqualsInvocation
import org.scalactic.TypeCheckedTripleEquals
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class TwoSumInArrayTest extends AnyWordSpec, Matchers, TypeCheckedTripleEquals{
  "twoSumInArray with Array(0, 1, 2, 7, 11, 15) and target 9" should {
    "return Array(2,3)" in {
      val result = TwoSumInArray.twoSum(Array(0, 1, 2, 7, 11, 15), 9)
      result should ===(Array(2,3))
    }
  }

  "twoSumGeneric() with Array(0, 1, 2, 7, 11, 15) and target 9" should {
    "return Array(2,3)" in {
      val result = TwoSumInArray.twoSumGeneric(List(0, 1, 2, 7, 11, 15), 9)
      result should ===(List(2, 3))
    }
  }
}
