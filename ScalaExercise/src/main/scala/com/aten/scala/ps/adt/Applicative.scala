package com.aten.scala.ps.adt

trait Applicative[F[_]] extends Functor[F]  {
  def map2[A,B,C]( fa: F[A], fb: F[B])( f: (A,B) => C ) : F[C]
  def unit[A]( a: =>A ) : F[A]
  
  def map[A,B]( fa: F[A])( f: A=>B ) : F[B] =
    map2(fa, unit(()))( (fa, _ ) => f(fa))
    
  def traverse[A,B]( as: List[A] )( f: A => F[B]) : F[List[B]] =
    as.foldRight(unit((List[B]())))((a,fbs) => map2(f(a), fbs)(_::_))
    
  def sequence[A]( lfa: List[F[A]] ) : F[List[A]] =
    traverse(lfa)( fa => map(fa)(a => a ))
    
  def replicateMe[A]( n : Int, ma:F[A]) : F[List[A]] = {
    @annotation.tailrec
    def nextM[A]( n :Int, a:A, l:List[A] ) : List[A]= n match {
      case i if i > 0 => nextM(i-1, a, a::l )
      case _ => l
    }
    map2(ma,unit()) ( (a,_) => nextM(n, a,List() ))
  }
    
}