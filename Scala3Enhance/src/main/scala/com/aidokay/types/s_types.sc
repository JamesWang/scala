import reflect.Selectable.reflectiveSelectable

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

trait M:
  def m(s: String): String = s

trait T1 extends M :
  override def m(s: String): String = s"[ ${super.m(s)} ]"

trait T2 extends M :
  override def m(s: String): String = s"( ${super.m(s)} )"

//Signals that the class is planned for extensions
//An open class typically comes with some documentation that describes the internal calling patterns
//between methods of the class as well as hooks that can be overridden
//--- extension contract of the class
open class C extends M :
  override def m(s: String): String = s"{ ${super.m(s)} }"

//Classes that are not open can still be extended, but only if meet one of the two alternative conditions:
//1. The extending class is in the same source file as the extended class.
//2. The "adhocExtensions" is enabled for the extending class:
//   using "import scala.language.adhocExtensions"
// or by compiler level with:
// -language:adhocExtensions
// in command line for compiler

val c12: C & T1 & T2 = new C with T1 with T2 //t2->t1->c
val c21: C & T2 & T1 = new C with T2 with T1 // t1->t2->c

println(c12.m("hello") == "( [ { hello } ] )")
println(c21.m("hello") == "[ ( { hello } ) ]")

// Intersection types support reasoning about types as sets and instances as members of a particular set
// A & B == B & A
//so
val c12a: C & T1 & T2 = c12
val c12b: C & T2 & T1 = c12
val c12c: T1 & C & T2 = c12
val c12d: T1 & T2 & C = c12
val c12e: T2 & C & T1 = c12
val c12f: T2 & T1 & C = c12

def f(t12: T1 & T2): String = t12.m("hello")
val list12: Seq[T1 & T2] = Seq(c12, c21)

//Union Types
case class Bad(message: String)

case class Good(i: Int)

val error = Bad("Failed")
val result = Good(0)

val seq1: Seq[Bad | Good] = Seq(error, result)

def work(i: Int): Good | Bad =
  if i > 0 then Bad(s"$i must be <= 0") else Good(i)

def process(result: Good|Bad) : String = result match
  case Bad(message) => message
  case Good(value) => s"Success! value=$value"

val results = Seq(0, 1).map(work)

//Rules for union and intersection types
//Both are distributive:
// A & ( B | C ) === (A & B) | (A & C )
// A | (B & C ) === ( A|B ) & (A | C )

trait Record extends reflect.Selectable:
  def id: Long

//Runtime Type:  persons: Seq[Record{name: String; age: Int}]
val persons = Seq("Dean" -> 29, "Dean" -> 29, "Dean" -> 30, "Fred" -> 30)
  .map{
    case (name1, age1) =>
      new Record:
        def id: Long = 0L
        val name: String = name1
        def age: Int = age1
  }

persons.map(p =>s"<${p.id}, ${p.name}, ${p.age}>")
  .foreach(println)

//structural type
type Observer = {
  def update(): Unit
}

trait Subject:
  protected var observers: Vector[Observer] = Vector.empty
  def addObserver(observer: Observer): Unit = observers :+= observer
  def notifyObservers(): Unit = observers.foreach(_.update())

case class Counter(start: Int = 0) extends Subject:
  var count = start
  def increment(): Unit =
    count += 1
    notifyObservers()

case class CounterObserver(counter: Counter):
  var updateCount = 0
  def update(): Unit = updateCount += 1


val c =  Counter()
c.increment()
val observer1 = CounterObserver(c)
c.addObserver(observer1)
c.increment()

val observer2 = CounterObserver(c)
c.addObserver(observer2)
c.increment()

println(c.count == 3)
//observer1 be notified twice
println(observer1.updateCount == 2)
//observer2 be notified once
println(observer2.updateCount == 1)

trait SubjectFunc:
  type State
  type Task = State => Unit
  private var observers: Vector[Task] = Vector.empty
  def addObserver(observer: Task): Unit =
    observers :+= observer

  def notifyObservers(state: State): Seq[Unit]=
    observers.map(task => task.apply(state))

case class Counter2(start: Int=0) extends SubjectFunc:
  type State = Int
  var count = start
  def increment(): Unit =
    count += 1
    notifyObservers(count)

case class CounterObserver2(var updateCalledCount: Int = 0):
  def apply(count: Int): Unit = updateCalledCount += 1

val observer21 = CounterObserver2()
val observer22 = CounterObserver2()

val c2 =  Counter2()
c2.increment()
c2.addObserver(observer21.apply)
c2.increment()
c2.addObserver(observer22.apply)
c2.increment()

println(c2.count == 3)
println(observer21.updateCalledCount == 2)
println(observer22.updateCalledCount == 1)