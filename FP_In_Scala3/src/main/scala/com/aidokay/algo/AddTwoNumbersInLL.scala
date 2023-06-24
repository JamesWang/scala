package com.aidokay.algo

import java.util
import scala.annotation.tailrec

object AddTwoNumbersInLL {

  def addTwoLinkedList(list1: util.LinkedList[Int], list2: util.LinkedList[Int]): util.LinkedList[Int] = {
    var up = 0
    val newList = util.LinkedList[Int]()
    var longer = Math.max(list1.size(), list2.size())
    while (longer > 0) {
      longer -= 1
      val v1 = if (list1.isEmpty) 0 else list1.removeLast()
      val v2 = if (list2.isEmpty) 0 else list2.removeLast()
      val sum = v1 + v2 + up
      up = if (sum >= 10) {
        newList.addLast(sum - 10)
        1
      } else {
        newList.addLast(sum)
        0
      }
    }
    newList
  }


  def addTwoLinkedList(list1: List[Int], list2: List[Int]): List[Int] = {
    var up = 0
    var newList = List[Int]()
    val r1 = list1.reverse
    val r2 = list2.reverse
    val max = Math.max(list1.size, list2.size)
    for (i <- 0 until max) {
      val v1 = if (i < list1.size) r1(i) else 0
      val v2 = if (i < list2.size) r2(i) else 0
      val sum = v1 + v2 + up

      up = if (sum >= 10) {
        val diff = sum - 10
        newList = newList :+ diff
        1
      } else {
        newList = newList :+ sum
        0
      }
    }
    newList
  }

  def addTwoLinkedListRecursive(list1: List[Int], list2: List[Int]): List[Int] = {
    @tailrec
    def add(acc: List[Int], up: Int, l1: List[Int], l2: List[Int]): List[Int] = {
      val (v1, t1)= l1 match
        case Nil => (0, Nil)
        case h::t => (h, t)
      val (v2, t2) = l2 match
        case  Nil => (0, Nil)
        case h::t => (h, t)

      val sum = v1 + v2 + up
      val (nUp, rm)  = if (sum >= 10){
        (1, sum - 10)
      } else {
        (0, sum)
      }
      if (t1.isEmpty && t2.isEmpty) {
        return acc :+ rm
      }
      add(acc :+ rm, nUp, t1, t2)
    }
    add(List(), 0, list1.reverse, list2.reverse)
  }
}
