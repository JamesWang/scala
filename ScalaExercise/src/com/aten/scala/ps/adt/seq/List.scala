package com.aten.scala.ps.adt.seq

sealed trait List[+A]
case object Nil extends List[Nothing]

case class Cons[+A]( head :A, tail : List[A]) extends List[A]  {
  override def toString : String = this match {
    case Cons(h,Nil) => h.toString() + ")"
    case Cons(h,t) => h +"," + t.toString()
  } 
    
   /* new StringBuilder("").append(head)
  .append(",").append( tail.toString() ).toString();*/
}
object List {
  def sum( ints:List[Int]) :Int = ints match {
    case Nil => 0
    case Cons(x,xs) => x+ sum(xs)
  }
  
  def sum2( ints:List[Int]) :Int = {
    
    @annotation.tailrec
    def sum( l:List[Int], acc:Int ) : Int = l  match {
      case Nil => acc
      case Cons(h,t) => sum( t, h + acc )
    }
    println("sum2")
    sum(ints,0 )
  }
  
  def product( ds:List[Double] ) : Double = ds match {
    case Nil => 1.0
    case Cons(0.0, _) =>0
    case Cons(h,t) => h * product(t)
  }
  
  def prod( ds:List[Double] ) : Double = {
    def pr( ds:List[Double], p :Double ) : Double = ds match {
      case Nil => 1.0 
      case Cons(0.0,_) =>0.0
      case Cons(h,t) => pr(t,h*p)
    }
    pr( ds, 1)
  }
  
  def apply[A]( as:A*) : List[A] = 
    if( as.isEmpty ) Nil
    else Cons(as.head, apply(as.tail:_*))
  
  @annotation.tailrec
  def drop[A]( l: List[A], n : Int ) :List[A] = l match {
      case Nil => Nil
      case Cons(h,t) if n>0 => drop( t, n - 1 )
      case _ => l
  }
  
  def dropWhile[A]( l:List[A], f : A=>Boolean ) :List[A] = l match {
    case Nil => Nil
    case Cons(h,t) => if( f(h) ) { println(h); dropWhile(t,f) } else Cons(h,dropWhile(t,f))
    //case Cons(h,t) if( !f(h)) => 
  }
  def foldRight[A,B]( as:List[A], z:B)( f: (A,B) => B ) : B = {
    @annotation.tailrec
    def fr( as:List[A],z:B )( f: (A,B)=>B ): B = as match {      
      case Cons(h,t) => fr(t,f(h,z))(f)
      case Nil => z
    }
    fr(as,z)(f)
  }
  
  def foldLeft[A,B](xs : List[A], z:B)( f: (A,B) => B ) : B = {
    @annotation.tailrec
    def fl( as: List[A], za: B )( f:(A,B) =>B) : B = as match {
      case Nil => za
      case Cons(h,t) => fl(t,f(h,za))(f)
    }
    fl(xs,z)(f)
  }
  
  def tail( ints:List[Int]) : List[Int] = ints match {
    case Nil => Nil
    case Cons(h,t) =>t
  }
  def setHeader(ints:List[Int], h:Int) : List[Int] = ints match {
    case Nil => Cons(h,Nil)
    case Cons(x,t) =>Cons(h,t)  
  }
  
 def append[A]( a:List[A], b:List[A]) : List[A] = a match {
   case Nil => b
   case Cons(h,Nil) => Cons(h,Nil)
   case Cons(h,t) => Cons(h,append(t,b))   
 }
 def filter[A]( as : List[A] )( f: A=>Boolean ) : List[A] = {
   def ft[A]( as: List[A], a: List[A]) ( f: A =>Boolean ) : List[A] =  as match {
     case Nil => a
     case Cons(h,t) => if( f(h) ) Cons(h,ft(t, a)(f)) else ft(t,a)(f)
   }
   ft( as, List() ) ( f ) 
 }
   
   
}

object runIt {
  import List._
   def main( args: Array[String] ) :Unit = {
    println( sum2(List(1,2,3,4,5)) )
    
    
    val x = List(1,2,3,4,5) match {
      case Cons(x,Cons(2,Cons(4,_))) => x
      case Nil => 42
      case Cons(x,Cons(y,Cons(3,Cons(4,_)))) => x + y 
      case Cons(h, t ) => h + sum(t)
      case _ => 101
    }
    println( x )
    
    println( drop(List(1,2,3,4,5), 2) )
    println( setHeader(List(2,3,4),1))
    println( dropWhile(List(1,2,3), (x:Int)=>x%2==0))
   // println( append(List(1,2,3), List(9,8,7)))
    println( foldRight(List(1,2,3,4,5),0)((x:Int,y:Int)=>x+y))
    println( filter(List(1,2,3,4,5,6,7))( (i:Int) => i%2 == 0 ) ) 
  }
}