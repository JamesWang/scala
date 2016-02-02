package com.aten.scala.ps.par.chp4.future

/**
 * @author jooly
 */
import scala.util.{ Try, Success, Failure }
import scala.concurrent._
import ExecutionContext.Implicits.global

object FuturesTry extends App {
  val threadName: Try[String] = Try(Thread.currentThread().getName)
  val someText: Try[String] = Try("try objects are synchronous")
  val message: Try[String] = for {
    tn <- threadName
    st <- someText
  } yield s"Message $st was create on t  =$tn"
  handleMessage(message)

  import aten.util.Utility._

  def handleMessage(t: Try[String]) = t match {
    case Success(msg) => log(msg)
    case Failure(error) => log(s"unexpected failure - $error")
  }
}