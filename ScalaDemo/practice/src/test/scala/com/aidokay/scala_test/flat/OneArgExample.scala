package com.aidokay.scala_test.flat

import java.io.{File, FileWriter}

import org.scalatest.flatspec.FixtureAnyFlatSpec

class OneArgExample extends FixtureAnyFlatSpec {

  case class FixtureParam(file: File, writer: FileWriter)

  override def withFixture(test: OneArgTest) = {
    val file = File.createTempFile("hello", "world") //create the fixture
    val writer = new FileWriter(file)
    val theFixture = FixtureParam(file, writer)

    try {
      writer.write("ScalaTest is ")
      withFixture(test.toNoArgTest(theFixture))
    } finally {
      //cleanup
    }
  }

  "Testing" should "be easy" in {
    f ⇒
      f.writer.write("easy!")
      f.writer.flush()
      assert(f.file.length === 18)
  }

  it should "be fun" in {
    f ⇒
      f.writer.write("fun!")
      f.writer.flush()
      assert(f.file.length === 17)
  }
}