import sp1.CsvEncoder
import sp1.CsvUtils.{createEncoder, writeCsv}
import shapeless.{:+:, ::, CNil, Coproduct, Generic, HList, HNil, Inl, Inr}


import java.time.LocalDate
import java.time.format.DateTimeFormatter

// use shapeless to derive type class instances for
//product types (i.e. case classes)

//1. have type class for head and tail of HList
//2. case class A, Generic[A] and a type class instance for generic's Repr

//IceCream
//String:: Int :: Boolean :: HNil

implicit val stringEncoder: CsvEncoder[String] =
  createEncoder(str => List(str))

implicit val booleanEncoder: CsvEncoder[Boolean] =
  createEncoder(b => if (b) List("yes") else List("no"))

implicit val intEncoder: CsvEncoder[Int] =
  createEncoder(i => List(i.toString))

implicit val hnilEncoder: CsvEncoder[HNil] =
  createEncoder(_ => Nil)

val dtFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
implicit val dateEncoder: CsvEncoder[LocalDate] =
  createEncoder(date => List(dtFormatter.format(date)))

implicit def hlistEncoder[H, T<: HList](
  implicit
  hEncoder: CsvEncoder[H],
  tEncoder: CsvEncoder[T]
):CsvEncoder[H::T] =
  createEncoder {
    case h :: t =>
      hEncoder.encode(h) ++ tEncoder.encode(t)
  }

val reprEncoder: CsvEncoder[String::Int::Boolean::HNil] = implicitly

reprEncoder.encode("abc":: 123::true::HNil)

/*
  object Generic {
    type Aux[A, R] = Generic[A] { type Repr = R }
  }
 */
implicit def genericEncoder[A, R](
  implicit
  //gen: Generic[A]{ type Repr = R}, //override Generic's Repr
  gen: Generic.Aux[A, R],
  enc: CsvEncoder[R]
): CsvEncoder[A] = createEncoder(a => enc.encode(gen.to(a)))

//needs implicit Encoder for LocalDate
case class Booking(room: String, date: LocalDate)

writeCsv(List(Booking("Lecture hall", LocalDate.now())))

//Deriving instances for coproducts(Sum)

sealed trait Shape
final case class Rectangle(width: Double, height: Double) extends Shape
final case class Circle(radius: Double) extends Shape

implicit val cnilEncoder: CsvEncoder[CNil] =
  createEncoder(_ => throw new Exception("Inconceivable!"))

implicit def coproductEncoder[H, T <: Coproduct](
  implicit
  hnilEncoder: CsvEncoder[H],
  tEncoder: CsvEncoder[T]
): CsvEncoder[H :+: T] = createEncoder{
  case Inl(h) => hnilEncoder.encode(h)
  case Inr(t) => tEncoder.encode(t)
}

implicit val dobuleEncoder : CsvEncoder[Double] = createEncoder(d=> List(d.toString))

val shapes: List[Shape] = List(
  Rectangle(3.0, 4.0),
  Circle(2.0)
)
writeCsv(shapes)