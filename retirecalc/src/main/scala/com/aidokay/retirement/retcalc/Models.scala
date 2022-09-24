package com.aidokay.retirement.retcalc

import cats.data.Reader
import cats.effect.IO
import com.aidokay.retirement.csv.Decoder
import com.aidokay.retirement.csv.Decoder.{FieldDecoder, Row, RowDecoder, main}
import com.aidokay.retirement.retcalc.Models.EquityData.{fromResource, loadFrom}
import com.aidokay.retirement.retcalc.Models.RowExtender

import scala.annotation.tailrec
import scala.deriving.Mirror.ProductOf
import scala.io.Source
import scala.util.Using

object Models extends RowExtender {

  trait RowExtender {
    extension (row: Row)
      def as[T](using p: ProductOf[T], d: RowDecoder[p.MirroredElemTypes]): T =
        p.fromProduct(d.decode(row))
  }
  case class RetCalcParams(nbOfMonthsInRetirement: Int, netIncome: Int, currentExpenses: Int, initialCapital: Double)

  case class EquityData(monthId: String, value: Double, annualDividend: Double) {
    val monthlyDividend: Double = annualDividend / 12
  }

  case class InflationData(monthId: String, value: Double)

  sealed abstract class RetCalcError(val message: String)

  type Result[T] = Either[RetCalcError, T]

  object RetCalcError {
    case class MoreExpensesThanIncome(income: Double, expenses: Double) extends RetCalcError(
      s"Expenses: $expenses >= $income, you will never be able to save enough to retire !"
    )

    case class ReturnMonthOutOfBounds(month: Int, maximum: Int) extends RetCalcError(
      s"Cannot get the return for month $month. Accepted range: 0 to $maximum")
  }

  //purely scala
  def loadedAs[T](resource: String)(using p: ProductOf[T], d: RowDecoder[p.MirroredElemTypes]): Vector[T] =
    Using.resource(Source.fromResource(resource)) { r =>
      r.getLines().drop(1).filter(_.trim.nonEmpty).map { line =>
        line
          .split("\t")
          .toList
          .as[T]
      }.toVector
    }

  ///Using Reader monad
  def loadFor[P](using p: ProductOf[P], d: RowDecoder[p.MirroredElemTypes]): Reader[String, Vector[P]] = Reader { resource =>
    DataLoader.load.run(resource).map(_.as[P])
  }

  object EquityData {
    def fromResource(resource: String): Vector[EquityData] = loadedAs[EquityData](resource)

    def loadFrom: Reader[String, Vector[EquityData]] = loadFor[EquityData]

  }

  object InflationData {
    def fromResource(resource: String): Vector[InflationData] = loadedAs[InflationData](resource)

    def loadFrom: Reader[String, Vector[InflationData]] = loadFor[InflationData]

  }

  def main(args: Array[String]): Unit = {
    //fromResource("sp500.tsv").foreach(println)
    //loadFrom.run("sp500.tsv").foreach(println)
    InflationData.loadFrom.run("cpi.tsv").foreach(println)
  }
}
