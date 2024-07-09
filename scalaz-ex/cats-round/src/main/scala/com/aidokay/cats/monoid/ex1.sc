import cats.implicits.catsSyntaxApplicativeErrorId
import cats.instances.int._
import cats.{Functor, Monad, MonadError, Monoid}

import scala.util.Try


sealed trait LoginError extends Product with Serializable

final case class UserNotFound(username: String) extends LoginError

final case class PasswordIncorrect(username: String) extends LoginError

case object UnexpectedError extends LoginError


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

implicit def boxPrintable[A: Printable]: Printable[Box[A]] = box => format(box.value)

format(Box("Hello world-box"))

trait Codec[A] {
  def encode(value: A): String

  def decode(value: String): A

  def imap[B](dec: A => B, enc: B => A): Codec[B] = new Codec[B] {
    override def encode(value: B): String = Codec.this.encode(enc(value))

    override def decode(value: String): B = dec(Codec.this.decode(value))
  }

}

def encode[A: Codec](value: A): String = implicitly[Codec[A]].encode(value)
def decode[A: Codec](value: String): A = implicitly[Codec[A]].decode(value)

implicit val stringCodec: Codec[String] = new Codec[String] {
  override def encode(value: String): String = value

  override def decode(value: String): String = value
}

implicit val intCodec: Codec[Int] = stringCodec.imap(_.toInt, _.toString)
implicit val doubleCodec: Codec[Double] = stringCodec.imap(_.toDouble, _.toString)

implicit def boxCode[A: Codec]: Codec[Box[A]] = implicitly[Codec[A]].imap(Box(_), _.value)

encode(Box(123.44))

import cats.syntax.flatMap._
import cats.syntax.functor._

def sumSquare[F[_] : Monad](a: F[Int], b: F[Int]): F[Int] = for {
  x <- a
  y <- b
} yield x * x + y * y

sumSquare(Option(2), Option(4))

import cats.syntax.either._

def countPositive(nums: List[Int]): Either[String, Int] = nums.foldLeft(0.asRight[String]) { (acc, num) =>
  if (num > 0) {
    acc.map(_ + 1)
  } else {
    Left("Negative. Stopping")
  }
}

countPositive(List(1, 2, 3))


case class User(userName: String, password: String)

type LoginResult = Either[LoginError, User]

def handleError(error: LoginError): Unit = error match {
  case UserNotFound(u) => println(s"User not found: $u")
  case PasswordIncorrect(u) => println(s"Password incorrect: $u")
  case UnexpectedError => println(s"Unexpected error")
}

val result1: LoginResult = User("dave", "passw0rd").asRight

def validateAdult[F[_]](age: Int)(implicit me: MonadError[F, Throwable]): F[Int] = {
  if (age >= 18) {
    me.pure(age)
  } else {
    new IllegalArgumentException("Age must be >= 18").raiseError[F, Int]
  }
}

validateAdult[Try](18)
validateAdult[Try](8)