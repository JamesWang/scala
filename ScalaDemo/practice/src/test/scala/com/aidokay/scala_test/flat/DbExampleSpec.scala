package com.aidokay.scala_test.flat

import java.io.{File, FileWriter}
import java.util.UUID.randomUUID

import com.aidokay.scala_test.DbServer.{createDb, Db}
import org.scalatest.flatspec.AnyFlatSpec

class DbExampleSpec extends AnyFlatSpec {

  def withDatabase(testCode: Db ⇒ Any): Unit = {
    val dbName = randomUUID.toString
    val db: Db = createDb(dbName)

    try {
      db.append("ScalaTest is ")
      testCode(db)
    } finally {
      //clean up
    }
  }

  def withFile(testCode: (File, FileWriter) ⇒ Any): Unit = {
    val file = File.createTempFile("hello", "world")

    //create the fixture
    val writer = new FileWriter(file)
    try {
      writer.write("ScalaTest is ")
      //setup the fixture
      testCode(file, writer)
    } finally {
      //clean up the fixture
    }

    "Testing" should "be productive" in withFile {
      (file, writer) ⇒
        writer.write("productive!")
        writer.flush()
        assert(file.length === 24)
    }

    "Test code" should "be readable" in withDatabase {
      db ⇒
        db.append("readable!")
        assert(db.toString === "ScalaTest is readable!")
    }

    it should "be clear and concise" in withDatabase {
      db ⇒
        withFile { (file, writer) ⇒
          db.append("clear!")
          writer.write("concise!")
          writer.flush()
          assert(db.toString === "ScalaTest is clear!")
          assert(file.length === 21)
        }
    }
  }
}
