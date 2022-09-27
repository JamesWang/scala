package com.aidokay.scala_test.flat

import org.scalatest.flatspec.AnyFlatSpec

import scala.collection.mutable.ListBuffer

class ExampleSpec extends AnyFlatSpec {

  def fixture = new {
    val builder = new StringBuilder("ScalaTest is ")
    val buffer = new ListBuffer[String]
  }

  "Testing" should "be easy" in {
    val f = fixture
    f.builder.append("easy!")
    assert(f.builder.toString === "ScalaTest is easy!")
    assert(f.buffer.isEmpty)
    f.buffer += "sweet"
  }

  it should "be fun" in {
    val f = fixture
    f.builder.append("fun!")
    assert(f.builder.toString === "ScalaTest is fun!")
    assert(f.buffer.isEmpty)
  }
}
