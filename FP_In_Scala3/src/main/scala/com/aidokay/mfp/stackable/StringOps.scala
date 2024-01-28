package com.aidokay.mfp.stackable

object StringOps {

  abstract class StringWriter:
    def write(data: String): String

  class BasicStringWriter extends StringWriter:
    override def write(data: String): String =
      s"Writing the following data: $data"

  trait Capitalizing extends StringWriter:
    abstract override def write(data: String): String =
      println("capitaling...")
      super.write(data.split("\\s+").map(_.capitalize).mkString(" "))

  trait LetterCasing extends StringWriter:
    val f: String => String

    abstract override def write(data: String): String =
      println("LetterCasing...")
      super.write(f.apply(data))

  trait UpperCasing extends LetterCasing:
    override val f: String => String = _.toUpperCase

    abstract override def write(data: String): String = {
      println("toUpperCase...")
      super.write(data)
    }

  trait LowerCasing extends LetterCasing:
    override val f: String => String = _.toLowerCase

    abstract override def write(data: String): String = {
      println("toLowerCase...")
      super.write(data)
    }


  def main(args: Array[String]): Unit = {
    val writer1 = new BasicStringWriter with UpperCasing with Capitalizing
    val writer2 = new BasicStringWriter with Capitalizing with LowerCasing

    println("----------------------------writer 1----------------------------------")
    println(s"Writer1: '${writer1.write("stackable trait")}'")
    //Writer1: 'Writing the following data: STACKABLE TRAIT'
    //Note:
    // the operations are applied from the right to left based on linearization of the mixin
    // linearization result: BasicStringWriter -> Capitalizing -> UpperCasing -> LetterCasing
    //so Capitalizing applied first, then UpperCasing applied,  and the result is as displayed
    println("----------------------------writer 2----------------------------------")
    println(s"Writer2: '${writer2.write("stackable trait")}'")
    //Writer2: 'Writing the following data: Stackable Trait'
  }

}
