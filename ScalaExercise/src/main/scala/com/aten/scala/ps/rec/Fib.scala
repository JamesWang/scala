package com.aten.scala.ps.rec

object Fib {
  def fibF( n : Int ) : List[Int] = {
    
    @annotation.tailrec
    def fib( m:Int, y1:Int, y2:Int, l:List[Int] ) : List[Int] = m match {
      case 0 => y1 + y2::l
      case _ =>
        fib(m-1,y2,y2+y1,y2+y1::l)
        
    }
    fib(n,1,0,List[Int](0))
  }
  //0,1,1,2,3,5...
  //1,1,2,3,5,
  //The important part is zip with the tail, that is shift one item left and add the items
  lazy val fibs : Stream[BigInt] = BigInt(0) #::BigInt(1) #:: fibs.zip(fibs.tail).map{
    n=>println(n); n._1+n._2
  }
  
  
  
}

object RunFib extends App {
  import Fib._
  
  println(fibF(10).reverse)
  fibs.take(10).foreach { x => x }
}