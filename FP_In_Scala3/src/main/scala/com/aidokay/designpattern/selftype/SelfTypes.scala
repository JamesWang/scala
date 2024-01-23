package com.aidokay.designpattern.selftype

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

object SelfTypes {

  trait Database[T]:
    def save(data: T): Unit


  trait InMemoryDatabase[T] extends Database[T]:
    val db: mutable.ListBuffer[T] = ListBuffer.empty

    override def save(data: T): Unit =
      println("Saving to in memory database")
      db.+=:(data)


  trait FileDatabase[T] extends Database[T]:
    override def save(data: T): Unit =
      println("Saving to file")

  trait History:
    def add(): Unit = println("Action added to history")

  trait Mystery:
    def add(): Unit = println("Mystery added!")


  trait Persister[T] {
    this: Database[T] with History with Mystery =>
    def persist(data: T): Unit = {
      println("Calling persist")
      save(data)
      add()
    }
  }
}
