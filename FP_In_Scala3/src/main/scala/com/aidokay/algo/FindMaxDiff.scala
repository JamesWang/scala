package com.aidokay.algo

import scala.annotation.tailrec
import scala.collection.mutable

object FindMaxDiff {
  //1 - Int
  def maxIntDiff(list: List[Int]): Int =
    @tailrec
    def findMaxDiff(max: Int, diff: Int, as: List[Int]): Int = as match
      case Nil => diff
      case h :: t if h > max => findMaxDiff(h, diff, t)
      case h :: t => findMaxDiff(max, Math.max(diff, max - h), t)

    val rev = list.reverse
    findMaxDiff(Int.MinValue, rev.head, rev.tail)

  //2 - Generic way
  def maxDiff[T: Ordering : Numeric](list: List[T], min: T): T =
    @tailrec
    def findMaxDiff(max: T, diff: T, as: List[T]): T = as match
      case Nil => diff
      case h :: t if Ordering[T].compare(h, max) > 0 => findMaxDiff(h, diff, t)
      case h :: t => findMaxDiff(max, Numeric[T].max(diff, Numeric[T].minus(max, h)), t)

    val rev = list.reverse
    findMaxDiff(min, rev.head, rev.tail)


  //3 - with Max/Min Pair
  def maxDiffInfo[T: Ordering: Numeric](list: List[T], min: T): (T, (T, T)) =
    @tailrec
    def findMaxDiff(max: T, diff: T, as: List[T], pair: (T, T)): (T, (T, T)) = as match
      case Nil => (diff, pair)
      case h::t if Ordering[T].compare(h, max) > 0 => findMaxDiff(h, diff, t, pair)
      case h::t =>
        val nMaxDiff = Numeric[T].max(diff, Numeric[T].minus(max, h))
        findMaxDiff(max, nMaxDiff, t, (if(Numeric[T].compare(nMaxDiff, diff) != 0) then (h, max) else pair))

    val rev = list.reverse
    findMaxDiff(min, rev.head, rev.tail, (min, min))
}
