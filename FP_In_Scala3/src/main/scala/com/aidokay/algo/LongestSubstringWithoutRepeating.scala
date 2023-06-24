package com.aidokay.algo

import scala.collection.mutable

object LongestSubstringWithoutRepeating {

  def lengthOfLongestSubstring(str: String): Int = {
    val unique = mutable.Set[Char]()
    var (maxLength: Int, curLength: Int) = (0, 0)
    str.toCharArray.foreach(ch => {
      if (unique.contains(ch)) {
        if (curLength > maxLength) maxLength = curLength
        curLength = 0
        unique.clear()
      } else {
        unique.add(ch)
        curLength += 1
      }
    })
    Math.max(maxLength, curLength)
  }
}
