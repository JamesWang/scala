package com.aten.scala.ps.par.chp2

/**
 * @author jooly
 */
import aten.util.Utility._
object Trans2 extends App {
  import Trans._
  
   def send0( a:Account, b:Account, n:Int)  = 
    a.synchronized{
      b.synchronized{
        a.money -= n 
        b.money += n
      }
    }
  def send( a:Account, b:Account, n:Int)  {
    def adjust() {
      a.money -= n
      b.money += n
    }
    if( a.uid < b.uid )
      a.synchronized{
        b.synchronized( adjust() )
      }
    else b.synchronized{ a.synchronized( adjust())}
  }

  val a = new Account("Jack",1000)
  val b = new Account("Jill",2000)
  import aten.util.Utility._
  val t1 = thread{ for( i<- 0 until 100) send(a,b,1)}
  val t2 = thread{ for( i<- 0 until 100) send(b,a,1)}
  t1.join();t2.join()
  Console.print(s" a = ${a.money}, b =${b.money}" )
}