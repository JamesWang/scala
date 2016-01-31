package aten.util

import scala.concurrent.Future
import scala.concurrent.Promise

/**
 * @author jooly
 */
object Utility {
  def thread(body: => Unit): Thread = {
    val t = new Thread {
      override def run() = body
    }
    t.start
    t
  }
  @volatile var dummy :Any = _;
  
  def log(s: String) {
    Console.println(s)
  }
  def timed[T]( body: =>T ) :Double ={
    val start = System.nanoTime()
    dummy = body
    val end = System.nanoTime()
    ((end-start)/1000000.0)
  }
  def warmedTimed[T]( n : Int = 200 )( body: =>T ): Double = {
    for( _ <- 0 until n ) body
    timed( body )
  }
  object MyTimer {
    import java.util._
    import scala.concurrent._
    private val timer = new Timer(true)
    def timeout( t:Long) :Future[Unit] ={
      val p = Promise[Unit]
      timer.schedule(new TimerTask{
        override def run = {
          p.success()
          timer.cancel()
          }
        }, t)
        p.future
    }
  }
  implicit class FutureOps[T]( val self : Future[T]) {
    import scala.concurrent._
    import ExecutionContext.Implicits.global
    def or( that:Future[T]) :Future[T] ={
      val p = Promise[T]
      self onComplete { case x => p.tryComplete(x)}
      that.onComplete { case y => p.tryComplete(y)}
      p.future
    }
  }
}