import shapeless.ops.hlist.{IsHCons, Last}
import shapeless.{:+:, ::, Coproduct, Generic, HList, HNil, Inl, Inr, Lazy, Witness, the}
import sp1.CsvEncoder
import sp1.CsvUtils.createEncoder


sealed trait Tree[A]

case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]

case class Leaf[A](value: A) extends Tree[A]

//be careful of the ::, explicitly import from shapeless, otherwise Scala's will be used
implicit def hlistEncoder[H, T <: HList](
                                          implicit
                                          hEncoder: Lazy[CsvEncoder[H]],
                                          tEncoder: CsvEncoder[T]
                                        ): CsvEncoder[H :: T] = createEncoder {
  case h :: t =>
    hEncoder.value.encode(h) ++ tEncoder.encode(t)
}

implicit def coproductEncoder[H, T <: Coproduct](
                                                  implicit
                                                  hEncoder: Lazy[CsvEncoder[H]],
                                                  tEncoder: CsvEncoder[T]
                                                ): CsvEncoder[H :+: T] = createEncoder {
  case Inl(h) => hEncoder.value.encode(h)
  case Inr(t) => tEncoder.encode(t)
}

implicit def genericEncoder[A, R](
                                   implicit
                                   gen: Generic.Aux[A, R],
                                   rEncoder: Lazy[CsvEncoder[R]]
                                 ): CsvEncoder[A] = createEncoder { value =>
  //gen.to => to Repr
  rEncoder.value.encode(gen.to(value))
}


//CsvEncoder[Tree[Int]]

def getRepr[A](value: A)(implicit gen: Generic[A]): gen.Repr = gen.to(value)

case class Vec(x: Int, y: Int)

case class Rect(origin: Vec, size: Vec)

getRepr(Vec(1, 2))
getRepr(Rect(Vec(0, 0), Vec(5, 5)))

//dependent typing - the result type of getRepr depends on its value parameter
// via their type member
// Type parameters are useful as "inputs
// Type members are useful as "outputs"

//4.2

/*
  trait Last[L <: HList] {
    type Out
    def apply(in: L): Out
  }
*/


val last1 = Last[String :: Int :: HNil]

val last2 = Last[Int :: String :: HNil]

last1("foo" :: 123 :: HNil)
last2(321 :: "foo" :: HNil)

trait Second[L <: HList] {
  type Out
  def apply(in: L): Out
}

object Second{
  type Aux[L <: HList, O] = Second[L] { type Out = O}

  //Using Aux ensures the apply() method does not erase the type
  //members on summoned instances
  def apply[L <: HList](implicit inst: Second[L]): Aux[L, inst.Out] = inst
}


implicitly[Last[String::Int::HNil]] //Out type member is erased
Last[String::Int::HNil]

/*
res4: shapeless.ops.hlist.Last[shapeless.::[String,shapeless.::[Int,shapeless.HNil]]]
res5: shapeless.ops.hlist.Last[shapeless.::[String,shapeless.::[Int,shapeless.HNil]]]{type Out = Int}

So we should avoid implicitly when working with dependently typed funcঞons
*/
//Using
the[Last[String::Int::HNil]]
import Second.Aux
implicit def hlistSecond[A, B, Rest <: HList]: Aux[A::B::Rest,B] = {
  new Second[A :: B:: Rest] {
    type Out = B

    override def apply(in: A :: B:: Rest): B = in.tail.head
  }
}

val second1 = Second[String :: Boolean :: Int :: HNil]
val second2 = Second[String :: Int :: Boolean :: HNil]

second1("foo" :: true :: 123 :: HNil)
second2("bar" :: 321 :: false :: HNil)
//encoding all the free variables as type parameters
//
def lastField[A, Repr <: HList](input: A)(
 implicit
 gen: Generic.Aux[A, Repr],
 last: Last[Repr]
) : last.Out = last.apply(gen.to(input))

lastField(Rect(Vec(1,2), Vec(3,4)))

//  def head(implicit c : IsHCons[L])
case class Wrapper(value: Int)

def getWrappedValue[A, Repr <: HList, Head](input: A) (
  implicit
  gen: Generic.Aux[A, Repr],
  isHCons: IsHCons.Aux[Repr, Head, HNil]
): Head = gen.to(input).head

getWrappedValue(Wrapper(42))

import shapeless.syntax.singleton._
var x = 42.narrow

import shapeless.labelled.{FieldType, field}
val someNumber = 444
trait Cherries
//->> tags the expression on the right of the arrow with the singleton type
// of the literal expresson on the left
val numCherries = "numCherrires" ->> someNumber
field[Cherries](123)

def getFieldName[K, V](value: FieldType[K, V])(implicit witness: Witness.Aux[K]): K =
  witness.value

def getFieldValue[K, V](value: FieldType[K, V]): V = value
getFieldName(numCherries)
getFieldValue(numCherries)


val garfield = ("cat" ->> "Garfield") :: ("orange" ->> true) :: HNil