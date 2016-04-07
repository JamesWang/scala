package com.aten.scala.ps.st


case class Address( street: String, city:String)
case class Person( name:String, address:Address)

object toJson extends App{
 
  trait ToJSON {
    def toJSON( level : Int = 0 ) : String
    val INDENTATION = " "
    def indentation(level:Int = 0 ) : ( String, String ) =
      ( INDENTATION * level , INDENTATION * (level +1))
  }
  
  implicit class AddressToJSON( address:Address ) extends ToJSON {
    def toJSON( level: Int = 0 ) : String = {
      val ( outdent, indent ) = indentation(level)
      s"""{
       |${indent} "street":"${address.street}"
       |${indent} "city": "${address.city}"
       |$outdent}""".stripMargin
    }
  }
  
  
  implicit class PersonToJSON( person:Person ) extends ToJSON {
    def toJSON( level: Int = 0 ) : String = {
      val ( outdent, indent ) = indentation(level)
      s"""{
       |${indent} "name":"${person.name}"
       |${indent} "address": ${person.address}
       |$outdent}""".stripMargin
    }
  }
  
  val a = Address("1 Scala lane", "Anytown")
  val b = Person("Buck Trend",a)
  
  println(a.toJSON())
  println()
  println(b.toJSON())

}
