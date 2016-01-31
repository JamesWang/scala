package com.aten.scala.ps.adt

sealed trait MyList[+A]
case object MyNil extends MyList[Nothing]

case class Cons[+A]( head :A, tail : MyList[A]) extends MyList[A]  {
  override def toString : String = this match {
    case Cons(h,MyNil) => h.toString() + ")"
    case Cons(h,t) => h +"," + t.toString()
  } 
    
   /* new StringBuilder("").append(head)
  .append(",").append( tail.toString() ).toString();*/
}
object MyList {
  def sum( ints:MyList[Int]) :Int = ints match {
    case MyNil => 0
    case Cons(x,xs) => x+ sum(xs)
  }
  
  def sum2( ints:MyList[Int]) :Int = {
    
    @annotation.tailrec
    def sum( l:MyList[Int], acc:Int ) : Int = l  match {
      case MyNil => acc
      case Cons(h,t) => sum( t, h + acc )
    }
    println("sum2")
    sum(ints,0 )
  }
  
  def product( ds:MyList[Double] ) : Double = ds match {
    case MyNil => 1.0
    case Cons(0.0, _) =>0
    case Cons(h,t) => h * product(t)
  }
  
  def prod( ds:MyList[Double] ) : Double = {
    def pr( ds:MyList[Double], p :Double ) : Double = ds match {
      case MyNil => 1.0 
      case Cons(0.0,_) =>0.0
      case Cons(h,t) => pr(t,h*p)
    }
    pr( ds, 1)
  }
  
  def apply[A]( as:A*) : MyList[A] = 
    if( as.isEmpty ) MyNil
    else Cons(as.head, apply(as.tail:_*))
  
  @annotation.tailrec
  def drop[A]( l: MyList[A], n : Int ) :MyList[A] = l match {
      case MyNil => MyNil
      case Cons(h,t) if n>0 => drop( t, n - 1 )
      case _ => l
  }
  
  def dropWhile[A]( l:MyList[A], f : A=>Boolean ) :MyList[A] = l match {
    case MyNil => MyNil
    case Cons(h,t) => if( f(h) ) { println(h); dropWhile(t,f) } else Cons(h,dropWhile(t,f))
    //case Cons(h,t) if( !f(h)) => 
  }
  def foldRight[A,B]( as:MyList[A], z:B)( f: (A,B) => B ) : B = {
    @annotation.tailrec
    def fr( as:MyList[A],z:B )( f: (A,B)=>B ): B = as match {      
      case Cons(h,t) => fr(t,f(h,z))(f)
      case MyNil => z
    }
    fr(as,z)(f)
  }
  
  def foldLeft[A,B](xs : MyList[A], z:B)( f: (A,B) => B ) : B = {
    @annotation.tailrec
    def fl( as: MyList[A], za: B )( f:(A,B) =>B) : B = as match {
      case MyNil => za
      case Cons(h,t) => fl(t,f(h,za))(f)
    }
    fl(xs,z)(f)
  }
  
  def tail( ints:MyList[Int]) : MyList[Int] = ints match {
    case MyNil => MyNil
    case Cons(h,t) =>t
  }
  def setHeader(ints:MyList[Int], h:Int) : MyList[Int] = ints match {
    case MyNil => Cons(h,MyNil)
    case Cons(x,t) =>Cons(h,t)  
  }
  
 def append[A]( a:MyList[A], b:MyList[A]) : MyList[A] = a match {
   case MyNil => b
   case Cons(h,MyNil) => Cons(h,MyNil)
   case Cons(h,t) => Cons(h,append(t,b))
   
 }
}

object runIt {
  import MyList._
   def main( args: Array[String] ) :Unit = {
    println( sum2(MyList(1,2,3,4,5)) )
    
    
    val x = MyList(1,2,3,4,5) match {
      case Cons(x,Cons(2,Cons(4,_))) => x
      case MyNil => 42
      case Cons(x,Cons(y,Cons(3,Cons(4,_)))) => x + y 
      case Cons(h, t ) => h + sum(t)
      case _ => 101
    }
    println( x )
    
    println( drop(MyList(1,2,3,4,5), 2) )
    println( setHeader(MyList(2,3,4),1))
    println( dropWhile(MyList(1,2,3), (x:Int)=>x%2==0))
   // println( append(MyList(1,2,3), MyList(9,8,7)))
    println( foldRight(MyList(1,2,3,4,5),0)((x:Int,y:Int)=>x+y))
  }
}