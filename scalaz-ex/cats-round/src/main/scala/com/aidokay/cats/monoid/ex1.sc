import cats.{Functor, Monoid}
import cats.instances.int._

import java.awt.print.Printable

val v = Monoid[Int].combine(1, 3)
println(v)

import cats.instances.option._

Monoid[Option[Int]].combine(Option(32), Option(11))

trait SuperAdder[A] {
  def add(items: List[A]): A
}

import cats.syntax.semigroup._

implicit def addItems1[A: Monoid](): SuperAdder[A] =
  (items: List[A]) => items.foldLeft(Monoid[A].empty)(_ |+| _)

case class Order(totalCost: Double, quantity: Double)

implicit def orderMonoid(): Monoid[Order] = new Monoid[Order]() {

  override def empty: Order = Order(0.0, 0.0)

  override def combine(x: Order, y: Order): Order =
    Order(x.totalCost + y.totalCost, x.quantity + y.quantity)
}

sealed trait Tree[+A]

final case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]

final case class Leaf[A](value: A) extends Tree[A]

implicit val treeFunctor: Functor[Tree[_]] = new Functor[Tree] {
  override def map[A, B](fa: Tree[A])(f: A => B): Tree[B] = fa match {
    case Leaf(v) => Leaf(f(v))
    case Branch(l, r) => Branch(map(l)(f), map(r)(f))
  }
}

//represents a transformation from A to String
//its contramap accepts a function B => A and create new Printable[B]
trait Printable[A] {
  def format(value: A): String

  def contramap[B](f: B => A): Printable[B] = (value: B) => format(f(value))
}

def format[A: Printable](value: A): String = implicitly[Printable[A]].format(value)

implicit val stringPrintable: Printable[String] = s => s"'$s'"

implicit val boolPrintable: Printable[Boolean] = b => if (b) "yes" else "nos"

final case class Box[A](value: A)
implicit def boxPrintable[A:Printable]: Printable[Box[A]] = box => format(box.value)

format(Box("Hello world-box"))