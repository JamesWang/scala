//Bottom types
//scala.Null and scala.Nothing, special types that handles some "corner cases"
//scala.Null is the type of the null reference, it is a subclass of every reference class: every children of AnyRef
//Null is not compatible with value types
//Nothing is at the very bottom of Scala's class hierarchy, it is a sub-type of every other type
//Example:
//def divide(x: Int, y: Int): Int =
//    if y != 0 then x / y
//    else sys.error("can't divide by zero")
// Nothing is sub-type of Int, so this works

class Dollars(val amount: Int) extends AnyVal :
  override def toString: String = s"$$$amount"

val money = new Dollars(1_000_000)
money.amount

//intersection type - is a sub-type of all combinations of its constituent types
// B & I & F is sub-type of B, I, F, B&I, B&F, I&F, and reflexively, B&I, I&F, B&I&F

//union types indicates that on object is an instance of at least one mentioned type
//a union type is a super-type of all combinations of its constituent types

//Abstract members

//An abstract type in Scala is always a memeber of some class or trait
trait Abstract:
  type T

  def transform(x: T): T

  val initial: T
  val current: T

class Concrete extends Abstract :
  type T = String

  override def transform(x: String): String = x + x

  val initial = "hi"
  val current = initial
