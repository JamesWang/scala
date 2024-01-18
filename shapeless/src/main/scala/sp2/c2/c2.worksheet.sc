
import shapeless.Generic
import sp2.c2.shape.{Circle, Rectangle, Shape}

case class IceCream(name: String, numCherries: Int, inCone: Boolean)

//iceCreamGen: shapeless.Generic[IceCream]{type Repr = shapeless.::[String,shapeless.::[Int,shapeless.::[Boolean,shapeless.HNil]]]}
val iceCreamGen = Generic[IceCream]

val iceCream = IceCream("Sundae", 1, inCone = false)
//to: just like marshall of Json to Json, this is to Repr
val repr = iceCreamGen.to(iceCream)

//This is from Repr to Scala object(unmarshall)
val iceCream2 = iceCreamGen.from(repr)

//Generic co-products

import shapeless.{Coproduct, :+:, CNil, Inl, Inr}

case class Red()

case class Amber()

case class Green()

type Light = Red :+: Amber :+: Green :+: CNil

val red: Light = Inl(Red())
val green: Light = Inr(Inr(Inl(Green())))

//type Repr = shapeless.:+:[sp2.c2.shape.Circle,shapeless.:+:[sp2.c2.shape.Rectangle,shapeless.CNil]]
val gen = Generic[Shape]

gen.to(Rectangle(3.0, 4.0))
gen.to(Circle(1.0))


trait CsvEncoder[A] {
  def encode(value: A): List[String]
}

case class Employee(name: String, number: Int, manager: Boolean)

implicit val employeeEncoder: CsvEncoder[Employee] =
  new CsvEncoder[Employee] {
    override def encode(employee: Employee): List[String] =
      List(
        employee.name,
        employee.number.toString,
        if (employee.manager) "yes" else "no"
      )
  }

def writeCsv[A](values: List[A])(implicit enc: CsvEncoder[A]): String =
  values.map(value => enc.encode(value).mkString(",")).mkString("\n")


val employees: List[Employee] = List(
  Employee("Bill", 1, manager = true),
  Employee("Peter", 2, manager = false),
  Employee("Milton", 3, manager = false)
)

println(s"Employees: ${writeCsv(employees)}")