package sp1

import shapeless.labelled.FieldType
import shapeless.{HList, HNil, LabelledGeneric, Lazy, Witness}

object JsonEx {

  sealed trait JsonValue

  case class JsonObject(fields: List[(String, JsonValue)]) extends JsonValue

  case class JsonArray(items: List[JsonValue]) extends JsonValue

  case class JsonString(value: String) extends JsonValue

  case class JsonNumber(value: Double) extends JsonValue

  case class JsonBoolean(value: Boolean) extends JsonValue

  case object JsonNull extends JsonValue


  trait JsonEncoder[A] {
    def encode(value: A): JsonValue
  }

  object JsonEncoder {
    def apply[A: JsonEncoder]: JsonEncoder[A] = implicitly[JsonEncoder[A]]
  }

  def createJsonEncoder[A](func: A => JsonValue): JsonEncoder[A] =
    new JsonEncoder[A] {
      override def encode(value: A): JsonValue = func(value)
    }

  case class IceCream(name: String, numCherries: Int, inCone: Boolean)

  trait JsonObjectEncoder[A] extends JsonEncoder[A] {
    def encode(value: A): JsonObject
  }

  def createObjectEncoder[A](fn: A => JsonObject): JsonObjectEncoder[A] = {
    new JsonObjectEncoder[A] {
      override def encode(value: A): JsonObject = {
        println(s"encoding: $value ")
        fn(value)
      }
    }
  }


  object Instances {
    implicit val stringEncoder: JsonEncoder[String] = createJsonEncoder(JsonString)
    implicit val doubleEncoder: JsonEncoder[Double] = createJsonEncoder(JsonNumber)
    implicit val intEncoder: JsonEncoder[Int] = createJsonEncoder(JsonNumber(_))
    implicit val booleanEncoder: JsonEncoder[Boolean] = createJsonEncoder(JsonBoolean)

    implicit def listEncoder[A: JsonEncoder]: JsonEncoder[List[A]] = createJsonEncoder(
      list => JsonArray(list.map(JsonEncoder[A].encode(_)))
    )

    implicit def optionEncoder[A: JsonEncoder]: JsonEncoder[Option[A]] = createJsonEncoder(
      opt => opt.map(JsonEncoder[A].encode(_)).getOrElse(JsonNull)
    )

    implicit val hnilEncoder: JsonObjectEncoder[HNil] = createObjectEncoder(_ => JsonObject(Nil))

    import shapeless.::

    implicit def hlistObjectEncoder[K <: Symbol, H, T <: HList](
      implicit
      witness: Witness.Aux[K],
      hEncoder: Lazy[JsonEncoder[H]],
      tEncoder: JsonObjectEncoder[T]
      ): JsonObjectEncoder[FieldType[K, H] :: T] = {

      val fieldName: String = witness.value.name
      println(s"fieldName=$fieldName")
      createObjectEncoder { hlist =>
        val head = hEncoder.value.encode(hlist.head)
        val tail = tEncoder.encode(hlist.tail)
        JsonObject((fieldName, head) :: tail.fields)
      }
    }

    implicit def genericObjectEncoder[A, H] (implicit
      generic: LabelledGeneric.Aux[A, H],
      hEncoder: Lazy[JsonObjectEncoder[H]]
      ): JsonEncoder[A] =
        createObjectEncoder { value => hEncoder.value.encode(generic.to(value))}

  }

  def main(args: Array[String]): Unit = {
    val iceCream = IceCream("Sundae", 1, inCone = false)
    val iceCreamJson: JsonValue = JsonObject(
      List(
        "name" -> JsonString("Sundae"),
        "numCherries" -> JsonNumber(1),
        "inCone" -> JsonBoolean(false)
      )
    )

    //import shapeless.LabelledGeneric

    //val gen = LabelledGeneric[IceCream].to(iceCream)
    import Instances._
    println(JsonEncoder[IceCream].encode(iceCream))
  }
}
