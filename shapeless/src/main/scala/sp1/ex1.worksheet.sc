import sp1.CsvEncoder

sealed trait Shape

final case class Rectangle(width: Double, height: Double) extends Shape

final case class Circle(radius: Double) extends Shape

val rect: Shape = Rectangle(3.0, 4.0)
val circ: Shape = Circle(1.0)

def area(shape: Shape): Double =
  shape match {
    case Rectangle(w, h) => w * h
    case Circle(r) => math.Pi * r * r
  }

area(rect)
area(circ)

type Rectangle2 = (Double, Double)
type Circle2 = Double
type Shape2 = Either[Rectangle2, Circle2]

val rect2: Shape2 = Left((3.0, 4.0))
val circ2: Shape2 = Right(1.0)

def area2(shape: Shape2): Double =
  shape match {
    case Left((w, h)) => w * h
    case Right(r) => math.Pi * r * r
  }

import shapeless.{::, HNil, the}

val product: String :: Int :: Boolean :: HNil = "Sunday" :: 1 :: false :: HNil

val first = product.head
val second = product.tail.head
val rest = product.tail.tail

import shapeless.Generic

case class IceCream(name: String, numCherries: Int, inCone: Boolean)

//with a type member:
//type Repr = shapeless.::[String,shapeless.::[Int,shapeless.::[Boolean,shapeless.HNil]]]
// = String:: Int :: Boolean :: HNil
val iceCreamGen = Generic[IceCream]

val iceCream = IceCream("Sundae", 1, inCone = false)

//to means to Repr
val repr = iceCreamGen.to(iceCream)

//from means from Repr
val iceCream2 = iceCreamGen.from(repr)

case class Employee(name: String, number: Int, manager: Boolean)

val employee = Generic[Employee].from(Generic[IceCream].to(iceCream))

import shapeless.{:+:, CNil, Inl, Inr}

case class Red()

case class Amber()

case class Green()

//:+: roughly ~= Either
type Light = Red :+: Amber :+: Green :+: CNil

val red: Light = Inl(Red())

//          Red :+: Amber :+: Green :+: CNil
//                 .^Inr_________________________
//                      ......^Inr______________
//                         ...^Inl______
val green: Light = Inr(Inr(Inl(Green())))

val amber: Light = Inr(Inl(Amber()))


val gen = Generic[Shape]

gen.to(Rectangle(3.0, 4.0))

gen.to(Circle(1.0))


implicit val employeeEncoder: CsvEncoder[Employee] = new CsvEncoder[Employee] {
  override def encode(e: Employee): List[String] =
    List(
      e.name, e.number.toString, if (e.manager) "yes" else "no"
    )
}

def writeCsv[A: CsvEncoder](values: List[A]): String = {
  val encoder = implicitly[CsvEncoder[A]]
  //values.par.map(value => encoder.encode(value).mkString(",")).mkString("\n")
  values.map(value => encoder.encode(value).mkString(",")).mkString("\n")
}


val employees: List[Employee] = List(
  Employee("Bill", 1, manager = true),
  Employee("Peter", 2, manager = false),
  Employee("Milton", 3, manager = false)
)
writeCsv(employees)

implicit val iceCreamEncoder: CsvEncoder[IceCream] = new CsvEncoder[IceCream] {
  override def encode(ic: IceCream) =
    List(
      ic.name, ic.numCherries.toString, if (ic.inCone) "yes" else "no"
    )
}

val iceCreams: List[IceCream] = List(
  IceCream("Sundae", 1, inCone = false),
  IceCream("Cornetto", 0, inCone = true),
  IceCream("Banana Split", 0, inCone = false)
)

writeCsv(iceCreams)

implicit def pairEncoder[A, B](implicit aEncoder: CsvEncoder[A], bEncoder: CsvEncoder[B]): CsvEncoder[(A, B)] = {
  CsvEncoder.instance(p=>aEncoder.encode(p._1) ++ bEncoder.encode(p._2))
}

writeCsv(employees zip iceCreams)


the[CsvEncoder[IceCream]]
implicitly[CsvEncoder[IceCream]]  //implicitly does not infer type correctly

implicit val booleanEncoder: CsvEncoder[Boolean] =
/*new CsvEncoder[Boolean] {
  override def encode(value: Boolean): List[String] =
    if (value) List("yes") else List("no")
}*/
  CsvEncoder.instance(value => if (value) List("yes") else List("no"))


