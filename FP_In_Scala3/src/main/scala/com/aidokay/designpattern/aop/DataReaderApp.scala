package com.aidokay.designpattern.aop

import com.aidokay.designpattern.aop.DBReader.DataReaderImpl

object DataReaderApp {
  def main(args: Array[String]): Unit = {
    val dataReader = new DataReaderImpl() with LoggingDataReader
    println(s"I just read the following data efficiently: ${dataReader.readData()}")
    println(s"I just read the following data inefficiently: ${dataReader.readDataInefficiently()}")
  }
}
