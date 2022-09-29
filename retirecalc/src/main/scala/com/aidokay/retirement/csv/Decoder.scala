package com.aidokay.retirement.csv


import com.aidokay.retirement.csv.Decoder.Row

import scala.deriving.Mirror.ProductOf

object Decoder {
  type Row = List[String]

  extension (row: Row)
    def as[T](using p: ProductOf[T], d: RowDecoder[p.MirroredElemTypes]): T =
      p.fromProduct(d.decode(row))

  trait RowDecoder[A <: Tuple]:
    def decode(a: Decoder.Row): A

  trait FieldDecoder[A]:
    def decodeField(a: String): A

  def csvToProduct[P](row: Row)(using p: ProductOf[P], d: RowDecoder[p.MirroredElemTypes]): P =
    p.fromProduct(d.decode(row))

  given RowDecoder[EmptyTuple] with
    def decode(a: Row): EmptyTuple = EmptyTuple

  given FieldDecoder[Int] with
    def decodeField(x: String): Int = x.toInt

  given FieldDecoder[Boolean] with
    def decodeField(x: String): Boolean = x.toBoolean

  given FieldDecoder[String] with
    def decodeField(x: String): String = x

  given FieldDecoder[BigDecimal] with
    def decodeField(x: String): BigDecimal = BigDecimal(x)

  given FieldDecoder[Double] with
    def decodeField(x: String): Double = x.toDouble

  given[H: FieldDecoder, T <: Tuple : RowDecoder]: RowDecoder[H *: T] with
    def decode(row: Row): H *: T =
      summon[FieldDecoder[H]].decodeField(row.head)
        *:
        summon[RowDecoder[T]].decode(row.tail)

  def main(args: Array[String]): Unit = {
    case class Foo(i: Int, b: Boolean, s: String)

    import Decoder.*


    val foo = csvToProduct[Foo](List("42", "true", "Hello"))
    //val sp500 = csvToProduct[Sp500Divdend](List("1900.01", "6.10", "0.22"))
    println(foo)
  }
}
