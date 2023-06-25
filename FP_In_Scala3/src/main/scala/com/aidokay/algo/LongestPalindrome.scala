package com.aidokay.algo

object LongestPalindrome {

  def findLongestPalindrome(str: String): String = {
    val len = str.length
    if (len <= 1) return str

    var pMinStart: Int = 0
    var pMaxLen : Int= 0
    def result = str.substring(pMinStart, pMinStart + pMaxLen)
    var i = 0
    while (i < len) {
      // if maxLen >= remaining, then no possibility of finding a longer one, so return the current longest one
      if (pMaxLen / 2 >= len -i ) {
        return result
      }
      // - - - - - - -
      // i
      var left  = i
      var right = i
      //Moving right pointer until find two characters next to each other are diff
      while (right < len - 1 && str(right) == str(right+1))
        right = right + 1

      //assume i as middle, if left and right same, then expand the range to left -1 ~ right + 1
      while(left > 0 && right < len - 1 && str(left-1) == str(right+1)){
        left = left -1
        right = right + 1
      }
      val newLen = right - left + 1
      if (newLen > pMaxLen){
        pMaxLen = newLen
        pMinStart = left
      }
      //move i to next character
      i = i + 1
    }
    result
  }

}
