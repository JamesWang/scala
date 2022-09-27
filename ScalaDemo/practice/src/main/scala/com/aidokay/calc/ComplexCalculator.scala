package com.aidokay.calc


object ComplexCalculator {
  val DOMAIN : String =
    """
      |Expression ::=
      |   Addition ( left: Expression, right: Expression )
      |   Subtraction ( left: Expression, right: Expression )
      |   Division ( left: Expression, right: Expression )
      |   SquareRoot(value: Expression )
      |   Number(value: Int)
      |""".stripMargin

  final case class Failure[A](value: A) extends Sum[A, Nothing]

  final case class Success[B](value: B) extends Sum[Nothing, B]

  sealed trait Sum[+A, +B] {
    def fold[C](error: A ⇒ C, success: B ⇒ C): C = this match {
      case Failure(v) ⇒ error(v)
      case Success(v) ⇒ success(v)
    }

    def map[C](f: B ⇒ C): Sum[A, C] = this match {
      case Failure(v) ⇒ Failure(v)
      case Success(v) ⇒ Success(f(v))
    }

    def flatMap[AA >: A, C](f: B ⇒ Sum[AA, C]): Sum[AA, C] = this match {
      case Failure(value) ⇒ Failure(value)
      case Success(value) ⇒ f(value)
    }
  }

  sealed trait Expression {
    def eval: Sum[String, Double] = this match {
      case Addition(left, right) ⇒ lift2(left, right, (l, r) ⇒ Success(l + r))
      case Subtract(left, right) ⇒ lift2(left, right, (l, r) ⇒ Success(l - r))
      case Division(left, right) ⇒ lift2(left, right, (l, r) ⇒ {
        if (r != 0) Success(l / r) else Failure("Division by zero")
      })
      case SquareRoot(v) ⇒ v.eval.flatMap(value ⇒ {
        if (value < 0) Failure("Square root of negative number") else Success(math.sqrt(value))
      })
      case Number(v) ⇒ Success(v)
    }

    def lift2(l: Expression, r: Expression, f: (Double, Double) ⇒ Sum[String, Double]): Sum[String, Double] =
      l.eval.flatMap(left ⇒ r.eval.flatMap(right ⇒ f(left, right)))
  }

  final case class Addition(left: Expression, right: Expression) extends Expression

  final case class Subtract(left: Expression, right: Expression) extends Expression

  final case class Division(left: Expression, right: Expression) extends Expression

  final case class SquareRoot(v: Expression) extends Expression

  final case class Number(v: Double) extends Expression

}
