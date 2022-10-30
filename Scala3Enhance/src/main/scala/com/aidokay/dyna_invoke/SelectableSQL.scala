package com.aidokay.dyna_invoke

import collection.mutable.HashMap as HMap
import scala.reflect.ClassTag

object SelectableSQL {

  open class Record(elems: (String, Any)*) extends Selectable :
    private val fields = HMap.from(elems.toMap)

    def selectDynamic(name: String): Any = fields(name)

    def applyDynamic(operation: String, paramTypes: ClassTag[?]*)(args: Any*): Any =
      val fieldName = operation.drop("update".length)
      val fname = fieldName.head.toLower +: fieldName.tail
      fields += fname -> args.head

    override def toString: String = s"Record($fields)"

  type Person = Record {
    val name: String
    val age: Int

    def updateName(newName: String): Unit
    def updateAge(newAge: Int): Unit
  }

  def main(args: Array[String]): Unit = {
    val person = Record(
      "name" -> "Buck trends",
      "age" -> 29,
      "famous" -> false
    ).asInstanceOf[Person]

    println(person.selectDynamic("age"))
  }
}
