import shapeless._

//retrieve the second to last element
trait Penultimate[L] {
  type Out
  def apply(l: L): Out
}

object Penultimate {
  type Aux[L, O] = Penultimate[L]{ type Out = O } //this ensures type members are visible to summoned instances
  def apply[L](implicit p: Penultimate[L]): Aux[L, p.Out] = p
}

import shapeless.ops.hlist

//Init ---- access to all but the last element of this HList.
implicit def hlistPenultimate[L <: HList, M <: HList, O](
  implicit
  init : hlist.Init.Aux[L, M],
  last : hlist.Last.Aux[M, O]
): Penultimate.Aux[L, O] = new Penultimate[L] {
  type Out = O
  def apply(l: L): Out = last.apply(init.apply(l))
}

type BigList = String :: Int :: Boolean :: Double :: HNil
val bigList: BigList = "foo" :: 123 :: true :: 345.00 :: HNil

Penultimate[BigList].apply(bigList)

implicit class PenultimateOps[A](a: A) {
  def penultimate(implicit inst: Penultimate[A]): inst.Out =
    inst.apply(a)
}

bigList.penultimate


implicit def genericPenultimate[A, R, O ](
  implicit
  generic: Generic.Aux[A, R],
  penultimate: Penultimate.Aux[R, O]
): Penultimate.Aux[A, O] = new Penultimate[A] {
  type Out = O
  def apply(a: A): Out = penultimate.apply(generic.to(a))
}
case class IceCream(name: String, numCherries: Int, inCone: Boolean)

IceCream("Sundae", 1, inCone = false).penultimate
