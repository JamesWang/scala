package com.aten.scala.ps.par.chp2

/**
 * @author jooly
 */

import scala.collection._
import scala.annotation.tailrec
import java.util.concurrent.Callable
object SynchronizedPool extends App {
  private val tasks = mutable.Queue[() => Unit]()
  object Worker extends Thread {
    @volatile var terminated = false
    //setDaemon(true)

    def poll(): Option[() => Unit] = tasks.synchronized {
      while (!terminated && tasks.isEmpty) tasks.wait()
      if (!terminated) Some(tasks.dequeue()) else {
        Console.println("Draining...")
        if (!tasks.isEmpty) Some(tasks.dequeue()) else {
          Console.println("Shutdown now....")
          None
        }
      }
    }
    @tailrec
    override def run() = poll() match {
      case Some(task) =>
        task(); run()
      case None =>
    }
    /**
     * Shutdown with running all tasks enqueued
     */

    def shutdown() = {
      terminated = true
      tasks.synchronized {
        tasks.notify()
      }
    }

    import aten.util.Utility._
    def periodically(duration: Long)(b: => Unit): Unit = {
      thread {
        while (!terminated) {
          thread(b)
          Thread.sleep(duration)
        }
      }
    }

    def parallel2[A, B](a: => A, b: => B): (A, B) = {
      lazy val aVal: A = a
      lazy val bVal: B = b

      val t1 = thread {
        aVal
      }

      val t2 = thread {
        bVal
      }

      t1.join()
      t2.join()

      (aVal, bVal)
    }
  }
  object Worker2 extends Thread {
    setDaemon(true)
    def poll() = tasks.synchronized {
      while (tasks.isEmpty) tasks.wait()
      tasks.dequeue()
    }
    override def run() = while (true) {
      val task = poll()
      task()
    }
  }

  Worker.start
  def asynchronous(body: => Unit) = tasks.synchronized {
    tasks.enqueue { () => body }
    tasks.notify()
  }
  Worker.periodically(1000)(Console.println("break time"))
  asynchronous { Console.print("Hello ") }
  asynchronous { Console.println(", World! ") }
  val t = Worker.parallel2(() => 3, () => 5)
  Console.print(t)
  // Console.print(t._1,t._2)
  Thread.sleep(5000)
  Worker.shutdown()
}