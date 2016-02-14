package com.aten.scala.ps.st

trait Connection {
  def query( q :String ) : String
}
trait Logger {
  def log( l : String ) : Unit
}
trait RequiredServices {
  def makeDatabaseConnection : Connection
  def logger : Logger
}
trait TestServices extends RequiredServices {
  def makeDatabaseConnection = new Connection {
    def query( q: String ) : String = "Test"
  }
  def logger = new Logger {
    def log( l: String ) = println(l)
  }
}

trait ProductFinder {
  this:RequiredServices =>
    def findProduct( productId : String ) = {
      val conn = this.makeDatabaseConnection
      val ret = conn.query( productId )
      logger.log("querying database....")
      logger.log( ret )
    }
}
object FinderSystem extends ProductFinder with TestServices

object SelfTypeAnnotation extends App{
  import com.aten.scala.ps.kestrel.ShowMe._
  FinderSystem.findProduct("Laptop")
  FinderSystem.show( f=>println(f.getClass().getPackage) )
}