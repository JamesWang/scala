package sp2.c2

object shape {

  sealed trait Shape

  final case class Rectangle(width: Double, height: Double) extends Shape

  final case class Circle(radius: Double) extends Shape

  def area(shape: Shape): Double = {
    shape match {
      case Rectangle(width, height) => width * height
      case Circle(radius) => math.Pi * radius * radius
    }
  }

  def main(args: Array[String]): Unit = {
    val rect: Shape = Rectangle(3.0, 4.0)
    val circ: Shape = Circle(1.0)

    println(s"$rect area is:${area(rect)}")
    println(s"$circ area is: ${area(circ)}")

    import shapeless.{HList, ::, HNil}

    val product: String :: Int :: Boolean :: HNil = "Sunday" :: 1 :: false :: HNil

    val first = product.head
    val second = product.tail.head
    val rest = product.tail.tail
  }


}
