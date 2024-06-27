trait ContainerAt:
  type T
  val data: T
  def compare(other: T) = data.equals(other)


class StringContainer(val data: String) extends ContainerAt{
  override type T = String
}

trait Adder1:
  def sum[T: Numeric](a: T, b: T):T = ???

println(s"comparing: ${new StringContainer("example test").compare("some test")}")

trait Adder[T]:
  def sun(a: T, b: T): T

object Adder:
  def sum[T:Adder](a: T, b: T) = summon[Adder[T]].sun(a, b)

given adderInt: Adder[Int] = (a: Int, b: Int) => a + b

import Adder.*
println(s"sum of 1 + 2 is ${sum(1, 2)}")

