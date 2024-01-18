package com.aidokay.eda.fp

import cats.{Eq, Order, Show}
import cats.derived.*
import io.circe.{Codec, Decoder, Encoder}

object modeling {
  case class Person(name: String, age: Int)derives Eq, Order, Show

  case class Address(streetName: String, streetNumber: Int, flat: Option[String])derives Codec.AsObject

  abstract class NewType[A](using
    eqv: Eq[A],
    ord: Order[A],
    shw: Show[A],
    enc: Encoder[A],
    dec: Decoder[A]
  ):
    opaque type Type = A
    inline def apply(a: A): Type = a
    protected inline final def derive[F[_]](using ev: F[A]): F[Type] = ev
    extension (t: Type) inline def value: A = t
/*
    given Wrapper[A, Type] with
      def iso: Iso[A, Type] =
        Iso[A, Type](apply(_))(_.value)
*/
    given Eq[Type] = eqv
    given Order[Type] = ord
    given Show[Type] = shw
    given Encoder[Type] = enc
    given Decoder[Type] = dec
    given Ordering[Type] = ord.toOrdering


}
