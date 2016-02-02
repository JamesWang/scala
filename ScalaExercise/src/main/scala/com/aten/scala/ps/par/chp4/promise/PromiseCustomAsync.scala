package com.aten.scala.ps.par.chp4.promise

/**
 * @author jooly
 */
import scala.concurrent._
import ExecutionContext.Implicits.global
import scala.util.control.NonFatal

object PromiseCustomAsync extends App {
  def myFuture[T]( b: =>T ) :Future[T] ={
    val p = Promise[T]
    global.execute( new Runnable{
      override def run() = try{
        p.success(b)
      }catch{
        case NonFatal(e)=>p.failure(e)
      }
    })
    p.future
  }
  import aten.util.Utility._
  val f = myFuture{"naa" + "na" * 8 + " Katamari Damacy!"}
  f.foreach { case text => log(text)}
  Thread.sleep(1000);
}