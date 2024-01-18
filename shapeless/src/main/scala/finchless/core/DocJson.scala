package finchless.core

import argonaut.Json
import argonaut.Json.jString
import cats.data.NonEmptyList
import shapeless.{HList, LabelledGeneric, Lazy}

import java.time.LocalDate

trait DocJson[T] {
  def doc: Json

  def on[A]: DocJson[A] = new DocJson[A] {
    override def doc: Json = DocJson.this.doc
  }
}

trait DocJsonLowPriority {
  implicit def projectDocJsonHL[F, G <: HList](implicit gen: LabelledGeneric.Aux[F, G], encode: Lazy[DocJson[G]]): DocJson[F] = {
    DocJson(encode.value.doc)
  }
}

object DocJson extends DocJsonLowPriority {
  def apply[A: DocJson]: DocJson[A] = implicitly

  def apply[A](j: Json): DocJson[A] = new DocJson[A] {
    override def doc: Json = j
  }

  private def docJson[T](str: String) = DocJson[T](jString(str))

  def string[T]: DocJson[T] = docJson("String")
  def lDate[T]: DocJson[T] = docJson("LDate")
  def long[T]: DocJson[T] = docJson("Long")
  def double[T]: DocJson[T] = docJson("Double")
  def boolean[T]: DocJson[T] = docJson("Boolean")

  implicit val LDATE: DocJson[LocalDate] = lDate
  implicit val INT: DocJson[Long] = long
  implicit val LONG: DocJson[Long] = long
  implicit val STRING: DocJson[String] = string
  implicit val FLOAT: DocJson[Float] = double
  implicit val DOUBLE: DocJson[Double] = double
  implicit val DECIMAL: DocJson[BigDecimal] = double
  implicit val BOOLEAN: DocJson[Boolean] = boolean
 // implicit val HANDLE: DocJson[Handle] = docJson("handle")
  def createDocJson[A:DocJson, F[_]](name: String): DocJson[F[A]] = DocJson(Json(name -> DocJson[A].doc))
  implicit def optionOf[A: DocJson]: DocJson[Option[A]] = createDocJson("OptionOf")
  implicit def listOf[A: DocJson]: DocJson[List[A]] = createDocJson("ListOf")
  implicit def mapOf[A: DocJson]: DocJson[Map[String, A]] = createDocJson("MapOf")
  //implicit def nel[A: DocJson]: DocJson[NonEmptyList[A]] = listOf.onp[NonEmptyList]
}
