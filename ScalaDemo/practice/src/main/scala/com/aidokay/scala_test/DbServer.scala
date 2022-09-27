package com.aidokay.scala_test

import java.util.concurrent.ConcurrentHashMap

object DbServer {

  type Db = StringBuffer
  private val databases = new ConcurrentHashMap[String,Db]

  def createDb(name: String): Db = {
    val db = new StringBuffer
    databases.put(name, db)
  }

  def removeDb(name: String): Db ={
    databases.remove(name)
  }

}
