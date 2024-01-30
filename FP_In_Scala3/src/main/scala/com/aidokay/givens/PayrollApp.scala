package com.aidokay.givens

object PayrollApp {
  private case class Employee(name: String, id: Long)

  private trait PayrollProcessor[C, A] {
    def process(payees: Seq[A]): Either[String, Throwable]
  }

  private trait PayrollBase[C, A](payees: Seq[A]):
    def processPayroll(using pp: PayrollProcessor[C, A]): Either[String, Throwable] = pp.process(payees)

  private case class USPayroll[A](payees: Seq[A]) extends PayrollBase[USPayroll[_], A](payees = payees)

  private case class CanadaPayroll[A](payees: Seq[A]) extends PayrollBase[CanadaPayroll[_], A](payees = payees)

  private object PayrollProcessors {
    class PayrollProcessorImpl[C](which: String) extends PayrollProcessor[C, Employee]:
      override def process(payees: Seq[Employee]): Either[String, Throwable] =
        Left(s"$which employees are processed")

    implicit object USPayrollProcessor extends PayrollProcessorImpl[USPayroll[_]]("us")

    implicit object CanadaPayrollProcessor extends PayrollProcessorImpl[CanadaPayroll[_]]("canada")
  }

  def main(args: Array[String]): Unit = {
    import PayrollProcessors.*
    //default to using Vector. It’s faster than List for almost everything and more memory-efficient for larger-than-trivial sized sequences.
    val r = USPayroll(Vector(Employee("a", 1))).processPayroll
    println(r) //Left(us employees are processed)

    val c = CanadaPayroll(Vector(Employee("a", 1))).processPayroll
    println(c)
  }
}
