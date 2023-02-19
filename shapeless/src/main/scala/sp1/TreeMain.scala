package sp1

import sp1.CsvUtils.createEncoder
import sp1.TreeEx.{Branch, Leaf, Tree}

object TreeMain {
  implicit def treeCsvEncoder[T](implicit underlying: CsvEncoder[T]): CsvEncoder[Tree[T]] = {
    createEncoder{
      case Branch(left, right) => treeCsvEncoder.encode(left) ++ treeCsvEncoder.encode(right)
      case Leaf(value) => underlying.encode(value)
    }
  }

  def main(args: Array[String]): Unit = {
    CsvEncoder[Tree[Int]]
  }
}
