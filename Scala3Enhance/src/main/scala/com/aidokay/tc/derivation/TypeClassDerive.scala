package com.aidokay.tc.derivation

import scala.compiletime.{erasedValue, summonInline}
import scala.deriving.Mirror

object TypeClassDerive {

  trait Show[A]:
    def show(a: A): String

  inline def summonAll[T <: Tuple]: List[Show[?]] =
    inline erasedValue[T] match
      case _: EmptyTuple => Nil
      case _: (t *: ts) => summonInline[Show[t]] :: summonAll[ts]

  def showProduct[T](shows: =>List[TypeClassDerive.Show[_]]): Show[T] =
    (a: T) => a.asInstanceOf[Product].productIterator.zip(shows.iterator).map {
      case (p, s) => s.asInstanceOf[Show[Any]].show(p)
    }.mkString

  def showSum[T](s: Mirror.SumOf[T], shows: =>List[Show[_]]): Show[T] =
    (a: T) => shows(s.ordinal(a)).asInstanceOf[Show[Any]].show(a)

  object Show:
    inline given derived[T](using m: Mirror.Of[T]): Show[T]=
      lazy val shows = summonAll[m.MirroredElemTypes]
      inline m match
        case s: Mirror.SumOf[T] => showSum(s, shows)
        case _: Mirror.ProductOf[T] => showProduct(shows)


}
