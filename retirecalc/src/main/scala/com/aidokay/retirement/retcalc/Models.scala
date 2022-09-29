package com.aidokay.retirement.retcalc

import cats.data.Reader
import cats.effect.IO
import com.aidokay.retirement.csv.Decoder
import com.aidokay.retirement.csv.Decoder.{FieldDecoder, Row, RowDecoder, main}
import com.aidokay.retirement.loader.DataLoader
import com.aidokay.retirement.loader.DataLoader.{loadFor, loadedAs}
import com.aidokay.retirement.retcalc.Models.EquityData.{fromResource, loadFrom}

import scala.annotation.tailrec
import scala.deriving.Mirror.ProductOf
import scala.io.Source
import scala.util.Using

object Models {

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

  
  object EquityData {
    def fromResource(resource: String): Vector[EquityData] = loadedAs[EquityData](resource)

    def loadFrom: Reader[String, Vector[EquityData]] = loadFor[EquityData]

  }

  object InflationData {
    def fromResource(resource: String): Vector[InflationData] = loadedAs[InflationData](resource)

    def loadFrom: Reader[String, Vector[InflationData]] = loadFor[InflationData]

  }

  def main(args: Array[String]): Unit = {
    fromResource("sp500.tsv").foreach(println)
    //loadFrom.run("sp500.tsv").foreach(println)
    //InflationData.loadFrom.run("cpi.tsv").foreach(println)
  }
}
