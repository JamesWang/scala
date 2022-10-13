package com.aidokay.algo

import org.scalactic.TypeCheckedTripleEquals
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class TestFindMaxDiff extends AnyWordSpec, Matchers, TypeCheckedTripleEquals{

  "FindMaxDiff.maxIntDiff() with bigger value shown after smaller should return 7 " should {
    "return 5 with [7,1,5,3,6,4]" in {
      val maxDiff = FindMaxDiff.maxIntDiff(List(7,1,5,3,6,4))
      maxDiff should ===(5)
    }
  }

  "FindMaxDiff.maxDiff() with bigger value shown after smaller should return 7 " should {
    "return 5 with [7,1,5,3,6,4]" in {
      val maxDiff = FindMaxDiff.maxDiff[Int](List(7, 1, 5, 3, 6, 4), Int.MinValue)
      maxDiff should ===(5)
    }
  }

  "FindMaxDiff.maxDiffInfo() with bigger value shown after smaller should return 7 " should {
    "return 5 with [7,1,5,3,6,4]" in {
      val maxDiff = FindMaxDiff.maxDiffInfo[Int](List(7, 1, 5, 3, 6, 4), Int.MinValue)
      maxDiff should ===((5, (1, 6)))
    }
  }
}
