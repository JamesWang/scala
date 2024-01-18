package sp2.c3

object csv {

  //turn a value of type A into a row of cells in a csv file
  trait CsvEncoder[A] {
    def encode(value: A): List[String]
  }

  def writeCsv[A](values: List[A])(implicit encoder: CsvEncoder[A]): String =
    values.map(value => encoder.encode(value).mkString(",")).mkString("\n")
}
