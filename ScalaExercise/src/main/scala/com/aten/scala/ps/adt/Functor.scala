package com.aten.scala.ps.adt

trait Functor[F[_]] {
  def map[A,B]( ma : F[A] )( f: A => B ) : F[B]
}