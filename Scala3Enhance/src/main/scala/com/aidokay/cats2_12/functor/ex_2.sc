import cats.{Functor, Monoid}
import cats.instances.int.*
import cats.syntax.semigroup.*

case class Order(totalCost: Double, quantity: Double)

trait SuperAdder:
  def add1(items: List[Int]): Int =
    items.foldLeft(Monoid[Int].empty)(_ |+| _)

  def add[T: Monoid](items: List[T]): T =
    items.foldLeft(Monoid[T].empty)(_ |+| _)


given orderMonoid: Monoid[Order] = new Monoid[Order] {
  override def empty: Order = Order(0.0d, 0.0d)

  override def combine(x: Order, y: Order): Order =
    Order(x.totalCost + y.totalCost, x.quantity + y.quantity)
}

val func1 : Int => Double = (x: Int) => x.toDouble
val func2 : Double => Double = (y: Double) => y * 2

val f1 = func1.andThen(func2)
val f2 = func2.compose(func1)
f1(2) == f2(2)

import cats.implicits.toFunctorOps
def doMath[F[_]: Functor](start: F[Int]): F[Int] =
  start.map(n=> n + 1 * 2)

doMath(List(1,2,3))
final case class Box[A](value: A)
given boxFunctor: Functor[Box] = new Functor[Box]:
  override def map[A, B](fa: Box[A])(f: A => B) =
    Box(f(fa.value))

val box = Box[Int](123)
box.map(value => value + 1)

sealed trait Tree[+A]
final case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]
final case class Leaf[A](value: A) extends Tree[A]
object Tree {
  def branch[A](left: Tree[A], right: Tree[A]) : Tree[A] =
    Branch(left, right)

  def leaf[A](value: A): Tree[A] = Leaf(value)
}

given treeFunctor: Functor[Tree] = new Functor[Tree] {
  override def map[A, B](fa: Tree[A])(f: A => B): Tree[B] = fa match
    case Branch(l, r) => Branch[B](map(l)(f), map(r)(f))
    case Leaf(v) => Leaf(f(v))
}

Tree.leaf(100).map(_*2)
Tree.branch(Tree.leaf(10), Tree.leaf(20).map(_*2))