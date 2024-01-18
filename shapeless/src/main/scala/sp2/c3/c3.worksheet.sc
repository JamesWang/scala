import sp2.c3.csv.{CsvEncoder, writeCsv}
import sp2.c3.entities.{Employee, IceCream}

val employees: List[Employee] = List(
  Employee("Bill", 1, manager = true),
  Employee("Peter", 2, manager = false),
  Employee("Milton", 3, manager = false)
)

println(s"Employees:\n${writeCsv(employees)}")


val iceCreams: List[IceCream] = List(
  IceCream("Sundae", 1, inCone = false),
  IceCream("Cornetto", 0, inCone = true),
  IceCream("Banana Split", 0, inCone = false)
)

println(s"IceCream:\n${writeCsv(iceCreams)}")

implicit def pairEncoder[A, B](
  implicit
  aEncoder: CsvEncoder[A],
  bEncoder: CsvEncoder[B]
): CsvEncoder[(A, B)] = pair => {
  aEncoder.encode(pair._1) ++ bEncoder.encode(pair._2)
}