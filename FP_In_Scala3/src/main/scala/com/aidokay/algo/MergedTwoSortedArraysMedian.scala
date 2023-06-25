package com.aidokay.algo

object MergedTwoSortedArraysMedian {
  def findMedianSortedArrays(nums1: Array[Int], nums2: Array[Int]): Double = {
    val totalLen = nums1.length + nums2.length
    val nArray = new Array[Int](totalLen)
    var j: Int = 0

    for (i <- 0 until totalLen) {
      if (i < nums1.length) {
        if (nums1(i) >= nums2(j)) {
          nArray(i) = nums2(j)
          j = j + 1
        } else {
          nArray(i) = nums1(i)
        }
      } else {
        nArray(i) = nums2(j)
        j = j + 1
      }
    }

    if ((nArray.length % 2) == 0) {
      (nArray(nArray.length / 2 - 1) + nArray(nArray.length / 2)) / 2.0
    } else {
      nArray(nArray.length / 2)
    }
  }
}
