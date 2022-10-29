## Use Type class derivation convert CSV to case class

#### Define type class and primitive types for the type classes

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
#### Usage:

             def loadFor[P](using p: ProductOf[P], d: RowDecoder[p.MirroredElemTypes]): Reader[String, Vector[P]] =
                 Reader {DataLoader.load.run(_).map(_.as[P])} 