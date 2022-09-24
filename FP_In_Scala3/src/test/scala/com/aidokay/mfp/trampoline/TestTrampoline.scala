package com.aidokay.mfp.trampoline

import org.scalactic.TypeCheckedTripleEquals
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class TestTrampoline extends AnyFlatSpec, Matchers, TypeCheckedTripleEquals {
  def fac(n: Int): Trampoline[BigInt] = {
    if (n == 0) Done(1)
    else FlatMap[BigInt, BigInt](More(() => fac(n - 1)), x => Done(n * x))
  }

  it should "calculate fac(5) returns 120" in {
    val result = fac(5).runT
    result should ===(BigInt(120))
  }

  it should "run fac(2000) succeed" in {
    fac(2000).runT
    assert(true)
  }

}
