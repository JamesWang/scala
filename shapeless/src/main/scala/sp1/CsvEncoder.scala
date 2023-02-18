package sp1

//Turn a value of type A into a row of cells in a CSV file
trait CsvEncoder[A] {
  def encode(value: A): List[String]
}


//idiomaঞc style for type class definiঞons includes a
//companion object containing some standard methods

object CsvEncoder {
  // "Summoner" method
  //def apply[A: CsvEncoder](): CsvEncoder[A] = implicitly[CsvEncoder[A]]
  def apply[A](implicit e: CsvEncoder[A]): CsvEncoder[A] = e

  //constructor method
  def instance[A](f: A => List[String]): CsvEncoder[A] =
    new CsvEncoder[A] {
      override def encode(value: A): List[String] = f(value)
    }
}