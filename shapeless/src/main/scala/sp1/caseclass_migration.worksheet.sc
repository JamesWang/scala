import shapeless.ops.hlist
import shapeless.{HList, LabelledGeneric}

case class IceCreamV1(name: String, numCherries: Int, inCone: Boolean)

case class IceCreamV2a(name: String, inCone: Boolean)

case class IceCreamV2b(name: String, inCone: Boolean, numCherries: Int)

case class IceCreamV2c(name: String, inCone: Boolean, numCherries: Int, numWaffles: Int)

trait Migration[A, B] {
  def apply(a: A): B
}

implicit class MigrationOps[A](a: A) {
  def migrateTo[B](implicit m: Migration[A, B]): B = m.apply(a)
}


implicit def genericMigration[A, B, ARepr <: HList, BRepr <: HList, Unaligned <: HList] (
  implicit
  aGen: LabelledGeneric.Aux[A, ARepr],
  bGen: LabelledGeneric.Aux[B, BRepr],
  inter: hlist.Intersection.Aux[ARepr, BRepr, Unaligned],
  align: hlist.Align[Unaligned, BRepr]
): Migration[A, B] = new Migration[A, B] {
  override def apply(a: A): B = bGen.from(align.apply(inter.apply(aGen.to(a))))
}
println("here")
IceCreamV1("Sundae", 1, inCone = true).migrateTo[IceCreamV2a]
IceCreamV1("Sundae", 1, inCone = true).migrateTo[IceCreamV2b]
