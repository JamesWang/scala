package com.aidokay.algo

import org.scalactic.TypeCheckedTripleEquals
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class LongestPalindromeTest extends AnyWordSpec, Matchers, TypeCheckedTripleEquals {

  "findLongestPalindrome() for 'babad'" should {
    "return 'aba' " in {
      val result = LongestPalindrome.findLongestPalindrome("babad")
      Set("bab", "aba") contains(result)
    }
  }

  "findLongestPalindrome() for 'cbbd'" should {
    "return 'bb' " in {
      val result = LongestPalindrome.findLongestPalindrome("cbbd")
      result should ===("bb")
    }
  }

  "findLongestPalindrome() for 'aaaaaa'" should {
    "return 'aaaaaa' " in {
      val result = LongestPalindrome.findLongestPalindrome("aaaaaa")
      result should ===("aaaaaa")
    }
  }

  "findLongestPalindrome() for '1234321'" should {
    "return '1234321' " in {
      val result = LongestPalindrome.findLongestPalindrome("1234321")
      result should ===("1234321")
    }
  }
}
