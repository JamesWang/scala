package com.aidokay.algo

import scala.collection.mutable

//Assume only one valid answer exists
object TwoSumInArray {
  def twoSum(nums: Array[Int], target: Int): Array[Int] = {
    val hMap = mutable.Map[Int, Int]()
    for (i <- nums.indices) {
      val diff = target - nums(i)
      hMap.get(diff) match
        case Some(index) => return Array(index, i)
        case None => hMap(nums(i)) = i
    }
    Array()
  }

  def twoSumGeneric[T: Numeric](numbers: List[T], target: T): List[Int] = {
    val hMap = mutable.Map[T, Int]()
    for (i <- numbers.indices) {
      val diff = Numeric[T].minus(target, numbers(i))
      hMap.get(diff) match
        case Some(index) => return List[Int](index, i)
        case None => hMap(numbers(i)) = i
    }
    Nil
  }
}
