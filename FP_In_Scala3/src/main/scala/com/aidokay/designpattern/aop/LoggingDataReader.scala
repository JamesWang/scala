package com.aidokay.designpattern.aop

import com.aidokay.designpattern.aop.DBReader.DataReader

trait LoggingDataReader extends DataReader {
  private def doRead[A](f: () => A): A = {
    val startMillis = System.currentTimeMillis()
    val result: A = f()
    val time = System.currentTimeMillis() - startMillis
    System.err.println(s"readData took $time milliseconds.")
    result
  }

  abstract override def readData(): List[DBReader.Person] = {
    doRead(super.readData)
  }

  abstract override def readDataInefficiently(): List[DBReader.Person] = {
    doRead(super.readDataInefficiently)
  }

}
