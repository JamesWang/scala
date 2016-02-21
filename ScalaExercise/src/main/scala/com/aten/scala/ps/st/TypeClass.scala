package com.aten.scala.ps.st

trait XmlConverter[A] {
  def toXml(a:A) : String
}

case class Movie( name:String, year:Int, rating:Double)

object Converters{  
  implicit object MovieConverter extends XmlConverter[Movie] {
    def toXml(a:Movie) = 
    <movie>
			<name>{a.name}</name>
			<year>{a.year}</year>
			<rating>{a.rating}</rating>
		</movie>.toString()		
  }
}
object ToXml {
  implicit def toXml2[A]( a: A ) = new {
      def convert( f: A=>String ) : String = {
          f(a)
      }
  }
}
object TypeClass extends App {
  import Converters._
  import ToXml._
  
  //def toXml[A]( a: A ) ( implicit converter: XmlConverter[A] ) =  converter.toXml(a)

  //A:XmlConverter  ---> A: F
  //XmlConverter[A] ---> F[A] 
  def toXml[A:XmlConverter]( a:A) = implicitly[XmlConverter[A]].toXml(a)
  
  //1.Type class    
  println( toXml(Movie("Incepiton",2010, 9.11)))
  
  //2.kestrel
  println( Movie("Incepiton",2010, 9.11).convert( MovieConverter.toXml ))
  
}