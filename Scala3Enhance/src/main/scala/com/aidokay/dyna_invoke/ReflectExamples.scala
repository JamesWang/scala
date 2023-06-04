package com.aidokay.dyna_invoke

//import scala.reflect.{universe => ru}

object ReflectExamples {

  trait PersonInterface {
    val name: String
    val age: Int
    def prettyPrint: String
  }

  case class Person(name: String, age: Int) extends PersonInterface {
    override def prettyPrint: String =
      s"""
         |Person{
         |  name: "$name",
         |  age: $age
         |}
         |""".stripMargin

    private val password = "123"
  }
  import scala.quoted.*
  def getPrintFields[T: Type](expr : Expr[T])(using Quotes): Expr[Any] = {
    import quotes.reflect._
    val fields = TypeTree.of[T].symbol.caseFields
    val accessors = fields.map(Select(expr.asTerm, _).asExpr)
    printAllElements(accessors)
  }
 // inline def printFields[A](elem : A): Unit = ${printFieldsImpl[A]('elem)}

  def printAllElements(list : List[Expr[Any]])(using Quotes) : Expr[Unit] = list match {
    case head :: other => '{ println($head); ${ printAllElements(other)} }
    case _ => '{}
  }
  def main(args: Array[String]): Unit = {
/*    val intType = ru.typeOf[Int]
    println(intType)

    val personType = ru.typeOf[Person]
    println(personType)
    val listType = ru.typeOf[List[_]]
    println(listType)*/
    case class Dog(name : String, favoriteFood : String, age : Int)
    //Test.printFields(Dog("wof", "bone", 10))
  }
}
