package com.aidokay.fp.ch5

import com.aidokay.fp.ch5.myLazyList.{MLazyStream, StreamExtender}
import org.scalactic.TypeCheckedTripleEquals
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class TestMyLazyStream extends AnyWordSpec, Matchers, TypeCheckedTripleEquals,StreamExtender {

  "MLazyStream.headOption" should {
    "return 'A' from myStream" in {
      val myStream: MLazyStream[String] = MLazyStream.apply("A", "B", "C", "D")
      val result: String = myStream.headOption.get
      result should ===("A")
    }
  }

  "MLazyStream.take(3)" should {
    "return Stream(A, B, C) from myStream" in {
      val myStream: MLazyStream[String] = MLazyStream.apply("A", "B", "C", "D")
      val result = myStream.take(3).toList
      result should ===(List("A", "B", "C"))
    }
  }

  "MLazyStream.drop(3)" should {
    "return Stream(D) from myStream" in {
      val myStream: MLazyStream[String] = MLazyStream.apply("A", "B", "C", "D")
      val result = myStream.drop(3).toList
      result should ===(List("D"))
    }
  }

  "MLazyStream.takeWhile()" should {
    "return Stream(2,4,6,8) from myStream" in {
      val myStream: MLazyStream[Int] = MLazyStream.apply(1,2,3,4,5,6,7,8,9,11,13)
      val result = myStream.takeWhile(_%2==0).toList
      result should ===(List(2,4,6,8))
    }
  }
}
