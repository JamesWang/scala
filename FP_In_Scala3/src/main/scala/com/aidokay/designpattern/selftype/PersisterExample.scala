package com.aidokay.designpattern.selftype

import com.aidokay.designpattern.selftype.SelfTypes.{FileDatabase, History, InMemoryDatabase, Mystery, Persister}

object PersisterExample {

  class FilePersister[T] extends Persister[T] with FileDatabase[T] with History with Mystery:
    override def add(): Unit = super[History].add()

  class MemoryPersister[T] extends Persister[T] with InMemoryDatabase[T] with History with Mystery:
    override def add(): Unit = super[Mystery].add()

  def main(args: Array[String]): Unit = {
    val fileStringPersister = new FilePersister[String]
    val memoryPersister = new MemoryPersister[Int]

    fileStringPersister.persist("Something, hello")
    fileStringPersister.persist("Something else, world")
    memoryPersister.persist(1000)
    memoryPersister.persist(1234)
  }

}
