package sp2.c3

import sp2.c3.csv.CsvEncoder

object entities {
  case class Employee(name: String, number: Int, manager: Boolean)

  implicit val employeeEncoder: CsvEncoder[Employee] = (employee: Employee) => List(
    employee.name,
    employee.number.toString,
    if (employee.manager) "yes" else "no"
  )

  case class IceCream(name: String, numCherries: Int, inCone: Boolean)

  implicit val iceCreamEncoder: CsvEncoder[IceCream] = (iceCream: IceCream) => {
    List(
      iceCream.name,
      iceCream.numCherries.toString,
      if (iceCream.inCone) "yes" else "no"
    )
  }
}
