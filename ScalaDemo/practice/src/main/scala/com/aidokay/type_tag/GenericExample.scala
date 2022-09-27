package com.aidokay.type_tag

import com.aidokay.type_tag.GenericInstances.TypeDetector

import scala.reflect.runtime.universe._

class GenericExample[T: TypeTag](a: String, b: T) {
  def fn(i: T): String = s"$a" + b + i
}

case class Example(a: String, b: Int) extends GenericExample[Int](a, b)
case class Example2(a: String, b: BigDecimal) extends GenericExample[BigDecimal](a, b)


object GenericInstances {
  implicit class TypeDetector[T: TypeTag](related: GenericExample[T]) {
    def getType: Type = typeOf[T]
  }
}

object GenericExampleApp {
  def main(args: Array[String]): Unit = {
    val classType = typeOf[Example].typeSymbol.asClass
    val baseClassType = typeOf[GenericExample[_]].typeSymbol.asClass
    val baseType = internal.thisType(classType).baseType(baseClassType)
    println(s"classType=$classType")
    println(s"baseClassType=$baseClassType")
    println(s"baseType=$baseType")

    println(baseType.typeArgs.head)

    println(Example2("", BigDecimal("123")).getType)
  }
}