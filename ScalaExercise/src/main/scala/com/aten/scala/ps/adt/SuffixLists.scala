package com.aten.scala.ps.adt

object SuffixLists extends App {
  
  def suffix1( xs:List[Int] ) : List[List[Int]] = xs match {
    case h::tail if tail == Nil =>  (h::List[Int]())::List[Int]()::List[List[Int]]()
    case h::tail => (h::tail)::suffix1(tail)
  }
  @annotation.tailrec
  def suffix( xs:List[Int], l: List[List[Int]]  ) : List[List[Int]] = xs match {
    case h::tail =>  suffix(tail,(h::tail) :: l)
    case Nil => (List()::l).reverse
  }
  println( "suffix one:" + suffix1(List(1,2,3,4)))
  println( "suffix two:" + suffix(List(1,2,3,4), List[List[Int]]()))
}