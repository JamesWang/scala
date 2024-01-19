package com.aidokay.algebraic

import com.aidokay.algegraic.Fishes.Fish
import com.aidokay.algegraic.SemigroupEx.Semigroup
import org.scalacheck.*
import org.scalacheck.Prop.*
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class SemigroupTest extends AnyWordSpec, Matchers {
  val fishGen: Gen[Fish] = for {
    weight <- Gen.posNum[Int]
    volume <- Gen.posNum[Int]
    poisonousness <- Gen.posNum[Int]
    teeth <- Gen.posNum[Int]
  } yield Fish(volume, weight, teeth, poisonousness)

  given arbFish: Arbitrary[Fish] = Arbitrary(fishGen)

  def associativity[S: Semigroup : Arbitrary]: Prop =
    forAll((a: S, b: S, c: S) => {
      println(s"a=$a")
      val sg = summon[Semigroup[S]]
      sg.op(sg.op(a, b), c) == sg.op(a, sg.op(b, c))
    })

  val prop: Properties = Properties("Fish")
  "run this test" should {
    " pass " in {
      println("test1")
      forAll { (a: String, b: String) =>
        println("here")
        (a + b).startsWith(a)
      }
    }
  }

  "associativity " should {
    "pass" in {
      associativity[Fish]
    }
  }
}
