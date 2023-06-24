package com.aidokay.algo

import org.scalactic.TypeCheckedTripleEquals
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class LongestSubstringTest extends AnyWordSpec, Matchers, TypeCheckedTripleEquals {

  "lengthOfLongestSubstring() for 'abcabcbb'" should {
    "be return 3" in {
      val result = LongestSubstringWithoutRepeating.lengthOfLongestSubstring("abcabcbb")
      result should ===(3)
    }
  }

  "lengthOfLongestSubstring() for 'bbbbb'" should {
    "be return 1" in {
      val result = LongestSubstringWithoutRepeating.lengthOfLongestSubstring("bbbb")
      result should ===(1)
    }
  }

  "lengthOfLongestSubstring() for 'pwwkew'" should {
    "be return 3" in {
      val result = LongestSubstringWithoutRepeating.lengthOfLongestSubstring("pwwkew")
      result should ===(3)
    }
  }
}
