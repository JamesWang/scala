package com.aidokay.tc

object JsonTC {
  trait JsonSerializer[T]:
    def serialize(t: T): String

    extension (t: T)
      def toJson: String = serialize(t)

  object JsonSerializer:
    given JsonSerializer[String] with
      override def serialize(s: String): String = s"\"$s\""

    given JsonSerializer[Int] with
      override def serialize(t: Int): String = t.toString

    given JsonSerializer[Long] with
      override def serialize(t: Long): String = t.toString

    given JsonSerializer[Boolean] with
      override def serialize(t: Boolean): String = t.toString

    given listSerializer[T] (using JsonSerializer[T]): JsonSerializer[List[T]] with
      override def serialize(ts: List[T]): String = s"[${ts.map(t => t.toJson).mkString(",")}]"

  object ToJsonMethods:
    extension[T] (a: T)(using jser: JsonSerializer[T])
      def toJson: String = jser.serialize(a)


  def main(args: Array[String]): Unit = {
    import ToJsonMethods.*
    println("tennis".toJson)
    println(10.toJson)
    println(true.toJson)
  }
}

