package com.aidokay.scalaz.ch4

import simulacrum.{op, typeclass}

object ord {
  @typeclass trait AOrdering[T] {
    def compare(x: T, y: T): Int

    @op("<") def lt(x: T, y: T): Boolean = compare(x, y) < 0

    @op(">") def gt(x: T, y: T): Boolean = compare(x, y) > 0
  }

  @typeclass trait ANumeric[T] extends AOrdering[T] {
    @op("+") def plus(x: T, y: T): T

    @op("*") def times(x: T, y: T): T

    @op("unary_-") def negate(x: T): T

    def zero: T

    def abs(x: T): T = if (lt(x, zero)) negate(x) else x
  }

  /*object ANumeric {
    def apply[T: ANumeric](): ANumeric[T] = implicitly[ANumeric[T]]

    object ops {
      implicit class ANumericOps[T](t: T)(implicit N: ANumeric[T]) {
        def +(o: T): T = N.plus(t, o)

        def *(o: T): T = N.times(t, o)

        def unary_- : T = N.negate(t)

        def abs: T = N.abs(t)

        def <(o: T): Boolean = N.lt(t, o)

        def >(o: T): Boolean = N.gt(t, o)
      }
    }
  }*/
  import java.math.{BigDecimal => BD}
  implicit val NumericBD: ANumeric[BD] = new ANumeric[BD] {
    override def plus(x: BD, y: BD): BD = x.add(y)

    override def times(x: BD, y: BD): BD = x.multiply(y)

    override def negate(x: BD): BD = x.negate()

    override def zero: BD = BD.ZERO

    override def compare(x: BD, y: BD): Int = x.compareTo(y)
  }
}
