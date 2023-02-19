package sp1

import shapeless.{:+:, ::, Coproduct, Generic, HList, HNil, Inl, Inr, Lazy}
import sp1.CsvUtils.createEncoder

import java.time.LocalDate
import java.time.format.DateTimeFormatter

//Turn a value of type A into a row of cells in a CSV file
trait CsvEncoder[A] {
  def encode(value: A): List[String]
}


//idiomaঞc style for type class definiঞons includes a
//companion object containing some standard methods

object CsvEncoder {
  // "Summoner" method
  //def apply[A: CsvEncoder](): CsvEncoder[A] = implicitly[CsvEncoder[A]]
  def apply[A](implicit e: CsvEncoder[A]): CsvEncoder[A] = e

  //constructor method
  def instance[A](f: A => List[String]): CsvEncoder[A] =
    new CsvEncoder[A] {
      override def encode(value: A): List[String] = f(value)
    }

  implicit val stringEncoder: CsvEncoder[String] =
    createEncoder(str => List(str))

  implicit val booleanEncoder: CsvEncoder[Boolean] =
    createEncoder(b => if (b) List("yes") else List("no"))

  implicit val hnilEncoder: CsvEncoder[HNil] =
    createEncoder(_ => Nil)

  val dtFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
  implicit val dateEncoder: CsvEncoder[LocalDate] =
    createEncoder(date => List(dtFormatter.format(date)))

  implicit val intEncoder: CsvEncoder[Int] =
    createEncoder(i => List(i.toString))

  implicit def listCsvEncoder[T](implicit underlying: CsvEncoder[T]): CsvEncoder[List[T]] = createEncoder(
    listT => listT.flatMap(underlying.encode)
  )


  //be careful of the ::, explicitly import from shapeless, otherwise Scala's will be used
  implicit def hlistEncoder[H, T <: HList](
    implicit
    hEncoder: Lazy[CsvEncoder[H]], // wrap in Lazy
    tEncoder: CsvEncoder[T]
  ): CsvEncoder[H :: T] = createEncoder {
    case h :: t =>
      hEncoder.value.encode(h) ++ tEncoder.encode(t)
  }

  implicit def coproductEncoder[H, T <: Coproduct](
    implicit
    hEncoder: Lazy[CsvEncoder[H]], // wrap in Lazy
    tEncoder: CsvEncoder[T]
  ): CsvEncoder[H :+: T] = createEncoder {
    case Inl(h) => hEncoder.value.encode(h)
    case Inr(t) => tEncoder.encode(t)
  }

  implicit def genericEncoder[A, R](
    implicit
    gen: Generic.Aux[A, R],
    rEncoder: Lazy[CsvEncoder[R]] // wrap in Lazy
  ): CsvEncoder[A] = createEncoder { value =>
    rEncoder.value.encode(gen.to(value))
  }
}