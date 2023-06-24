package com.aidokay.algo

import org.scalactic.TypeCheckedTripleEquals
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import java.util

class AddTwoNumbersInLLTest extends AnyWordSpec, Matchers, TypeCheckedTripleEquals {

  "addTwoLinkedList() for LinkedList(2,4,3) and LinkedList(5,6,4)" should {
    "return LinkedList(7,0,8)" in {
      val list1 = util.LinkedList[Int](util.Arrays.asList(2, 4, 3))
      val list2 = util.LinkedList[Int](util.Arrays.asList(5, 6, 4))

      val result = AddTwoNumbersInLL.addTwoLinkedList(list1, list2)
      result should ===(util.LinkedList(util.Arrays.asList(7, 0, 8)))
    }
  }

  "addTwoLinkedList() for LinkedList(2,4,3,5) and LinkedList(5,6,4)" should {
    "return LinkedList(9,9,9,2)" in {
      val list1 = util.LinkedList[Int](util.Arrays.asList(2, 4, 3, 5))
      val list2 = util.LinkedList[Int](util.Arrays.asList(5, 6, 4))

      val result = AddTwoNumbersInLL.addTwoLinkedList(list1, list2)
      result should ===(util.LinkedList(util.Arrays.asList(9, 9, 9, 2)))
    }
  }

  "addTwoLinkedList() for List(2,4,3) and List(5,6,4)" should {
    "return List(7,0,8)" in {
      val list1 = List(2, 4, 3)
      val list2 = List(5, 6, 4)

      val result = AddTwoNumbersInLL.addTwoLinkedList(list1, list2)
      result should ===(List(7, 0, 8))
    }
  }

  "addTwoLinkedList() for List(2,4,3,5) and List(5,6,4)" should {
    "return List(9,9,9,2)" in {
      val list1 = util.LinkedList[Int](util.Arrays.asList(2, 4, 3, 5))
      val list2 = util.LinkedList[Int](util.Arrays.asList(5, 6, 4))

      val result = AddTwoNumbersInLL.addTwoLinkedList(list1, list2)
      result should ===(util.LinkedList(util.Arrays.asList(9, 9, 9, 2)))
    }
  }
}
