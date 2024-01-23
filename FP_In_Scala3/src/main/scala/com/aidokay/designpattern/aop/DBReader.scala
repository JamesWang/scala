package com.aidokay.designpattern.aop

import org.json4s.*
import org.json4s.jackson.JsonMethods._

object DBReader {
  case class Person(firstName: String, lastName: String, age: Int)

  trait DataReader {
    def readData(): List[Person]

    def readDataInefficiently(): List[Person]
  }

  class DataReaderImpl extends DataReader:
    implicit val formats: Formats = DefaultFormats

    import AsJsonInput.streamAsJsonInput

    private def readUntimed(): List[Person] = ???

    /*parse(Thread.currentThread().getContextClassLoader.getResourceAsStream("person.json"))
      .extract[Person]
*/
    override def readData(): List[Person] = readUntimed()

    override def readDataInefficiently(): List[Person] = {
      (1 to 10000) foreach {
        num => readUntimed()
      }
      readUntimed()
    }

}
