package com.aten.scala.ps.par.chp4.await

import scala.concurrent._
object AsyncExample extends App {
  import scala.async.Async._
  import ExecutionContext.Implicits.global
  
  def delay( n:Int ) :Future[Unit] = async {
    /*
     * blocking{} call tells the execution context that the worker thread is blocked and 
     * allows it to temporarily spawn additional worker thread if necessary
     * 
     * The blocking statement is used inside asynchronous code to designate 
     * that the enclosed block of code contains a blocking call. it is not 
     * a blocking operation by itself
     */
    blocking {
      println("Sleeping")
      Thread.sleep( n * 1000 )
      println("Done")
    }
  }
  delay(5)
  
  import aten.util.Utility._
  async {
   log("T-minus 1 second")
   await { delay(1) }
   log("done2")
  }
  /** =>
   * Future {
   * 	log("T-minus 1 second")
   *  delay(1) foreach {
   *    case x => log("done")
   *  }
   * }
   */
  
  def countdown( n:Int ) ( f: Int => Unit ) : Future[Unit] = async {
    var i = n
    while( i > 0 ) {
      f( i ) 
      await{ delay(1) }
      i -= 1      
    }
  }
  countdown( 10 ) { n => log(s"T-minus $n seconds" ) } foreach {
    case _ => log(s" This program is over!")
  }
  
  Thread.sleep(15000);
}