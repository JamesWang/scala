package com.aten.scala.ps.par.chp4.promise

/**
 * @author jooly
 */
import scala.concurrent._
import ExecutionContext.Implicits.global
import aten.util.Utility._

object PromiseCancellation extends App {
  type Cancellable[T] = (Promise[Unit], Future[T])
  def cancellable[T]( b: Future[Unit]=>T ) :Cancellable[T] ={
    val cancel = Promise[Unit]
    val f = Future{
      val r = b( cancel.future )
      if( !cancel.tryFailure( new Exception) )
        throw new CancellationException
      r
    }
    ( cancel, f )
  }
  
  val ( cancel, value ) = cancellable{
    //Future => String
    cancel => // A Future
      var i = 0
      while( i < 5 ) {
        //Returns whether the future has already 
        //been completed with a value or an exception.
        if( cancel.isCompleted ) throw new CancellationException
        Thread.sleep(500)
        log(s"$i: working")
        i +=1
      }
      "resulting value" // T = String
  }
  Thread.sleep(1500)
  cancel.trySuccess()
  log("computation cancelled!")
  //log("value=" + value.value.get.get)
  Thread.sleep(2000)
}