package com.aidokay.scalaz.ch4

import eu.timepit.refined.api.Refined
import eu.timepit.refined.boolean.And
import eu.timepit.refined.collection.{MaxSize, NonEmpty}
import eu.timepit.refined.numeric.Positive
import eu.timepit.refined.refineV

object adt {
  sealed abstract class IList[A]

  final case class INil[A]() extends IList[A]

  final case class ICon[A](head: A, tail: IList[A]) extends IList[A]

  type |:[L, R] = Either[L, R]
  type Accepted = String |: Long |: Boolean

  type Name = NonEmpty And MaxSize[10]

  final case class Person private(
    name: String Refined Name,
    age: Int Refined Positive
  )


  object Person {
    def apply(name: String, age: Int): Either[String, Person] = {
      {
        for {
          nm <- refineV[Name](name)
          ag <- refineV[Positive](age)
        } yield new Person(nm, ag)
      }
    }.orElse(Left(s"bad input: name=$name, age=$age"))
  }

  def welcome(person: Person): String = s"${person.name} you look wonderful at ${person.age}"


  def main(args: Array[String]): Unit = {
    println(Person("", -1).map(welcome))
  }
}
