package com.aten.scala.ps.adt

import scala.collection.immutable.List

trait Monad[F[_]] extends Functor[F]{
  def unit[A]( a: =>A ) : F[A]
  def flatMap[A,B]( ma:F[A] )( f: A =>F[B] ) : F[B]
  
  def map[A,B] ( ma: F[A] )( f: A=>B) : F[B] = flatMap( ma )( a => unit(f(a)))
  def map2[A,B,C] ( ma : F[A], mb:F[B] ) ( f: (A,B)=> C ): F[C] =
    flatMap( ma )( a => map( mb )( b => f(a,b) ) )
    
  def sequence[A]( lfa: List[F[A]] ) : F[List[A]] =
    lfa.foldRight(unit(List[A]()))(
        (fa,fb) => flatMap(fa)( a => map(fb)( b => a::b))
    )
    
  def traverse[A,B]( la :List[A])(f: A =>F[B] ): F[List[B]] =
    sequence(la.flatMap { x => List(f(x)) })    
  
  def replicateMe[A]( n : Int, ma:F[A]) : F[List[A]] = {
    @annotation.tailrec
    def next[A]( n:Int, a:A, l:List[A] ): List[A]= n match {
      case i if( i > 0 )=> {
        next(n-1,a,a::l)
      }
      case _ => l
    }    
    flatMap( ma )( a => unit( next(n,a,List()))) 
  }
  
  def filterM2[A]( ms:List[A])( f: A => F[Boolean]) : F[List[A]] =  {
    @annotation.tailrec
    def fm[A]( ms:List[A], a: F[List[A]])( f:A=>F[Boolean]):F[List[A]] = ms match {
      case Nil => a
      case h::t =>
        fm(t,flatMap(f(h)){ b=> if( !b ) a else map(a)(ai=>(h::ai))})(f) 
    }
    map(fm(ms,unit(List[A]()))(f))(x=>x.reverse)
  }
  
  def filterM3[A]( ms:List[A])( f: A => F[Boolean]) : F[List[A]] =  {
    @annotation.tailrec
    def fm[A]( ms:List[A], a: List[A])( f:A=>F[Boolean]):F[List[A]] = ms match {
      case Nil => unit(a)
      case h::Nil => flatMap(f(h))( b=> if( !b ) unit(Nil) else unit(h::a))
      case h::t => fm(t,List(h))(f)
    }
    map(fm(ms,List[A]())(f))(x=>x.reverse)
  }
  
  def filterM[A]( ms:List[A])( f: A => F[Boolean]) : F[List[A]] =  ms match {
    case Nil =>unit(Nil)
    case h::t => {
      flatMap(f(h)){b=>
        if( !b ) filterM(t)(f)
        else map(filterM(t)(f))(h::_)
      }
    }
  }
  
  def join[A]( mma:F[F[A]] ) : F[A] =
    flatMap(mma)(fa => map(fa)(a => a ))
    
  def compose[A,B,C]( f : A=>F[B] , g: B=> F[C] ) : A=>F[C] = 
    a => flatMap(f(a))(b=>g(b))
}

object MonadApp extends App {
  val optionMonad = new Monad[Option]{
    override def unit[A]( a: => A ) : Option[A] = Option(a)
    override def flatMap[A,B](ma:Option[A])( f: A => Option[B] ) : Option[B] =
      ma.flatMap { a => f(a) }
  }
  val listMonad = new Monad[List]{
    override def unit[A]( a: => A ) : List[A] = List(a)
    override def flatMap[A,B](ma:List[A])( f: A => List[B] ) : List[B] =
      ma.flatMap { a => f(a) }
  }
  
  println( optionMonad.replicateMe(5000, Some('a')))
  println( optionMonad.sequence(List(Some('a'),Some('b'))))  
  println( optionMonad.filterM2(List(1,2,3,4))(a => Option[Boolean](a > 2)))
  println( optionMonad.filterM3(List(1,2,3,4))(a => Option[Boolean](a > 2))) 
  println( optionMonad.join(Option(Option(1))))  
  println( listMonad.join(List(List(1,2,4,5))))
  
  //optionMonad.compose(optionMonad.join[Int],optionMonad.unit[Int])(Option(Option(1)))
      
}