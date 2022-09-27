package com.aidokay.contract.mongo

import com.aidokay.contract.mongo.Helpers.GenericObservable
import org.mongodb.scala._

object MongoWriter {

  def connectThenWrite(jsonList: Seq[String]): Unit = {
    val mongoClient: MongoClient = MongoClient("mongodb://hostname")
    val database: MongoDatabase = mongoClient.getDatabase("DEV")
    val collection: MongoCollection[Document] = database.getCollection("contracts")
    val docs = jsonList.map(json⇒Document(json))
    try {
        println("writing.....")
        collection.insertMany(docs).printResults()
        //println(s"Total documents after insert ${insertAndCount.head}")
    }
    catch {
      case ex: Exception ⇒
        println(s"Exception happened:")
        ex.printStackTrace()
    }
  }

}
