package sp1

object CsvUtils {
  def writeCsv[A: CsvEncoder](values: List[A]): String =
    values.map(value => implicitly[CsvEncoder[A]].encode(value).mkString(",")).mkString("\n")

  def createEncoder[A](f: A => List[String]): CsvEncoder[A] =
    new CsvEncoder[A] {
      override def encode(value: A): List[String] = f(value)
    }
}
