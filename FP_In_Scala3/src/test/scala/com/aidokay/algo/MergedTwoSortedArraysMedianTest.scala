package com.aidokay.algo;

import org.scalactic.TypeCheckedTripleEquals
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class MergedTwoSortedArraysMedianTest extends AnyWordSpec, Matchers, TypeCheckedTripleEquals {
  "findMedianSortedArrays() for Array(1,2) and Array(3,4)" should {
    "returns 2.5" in {
      val result = MergedTwoSortedArraysMedian.findMedianSortedArrays(Array(1,2), Array(3,4))
      result should ===(2.5)
    }
  }
}
