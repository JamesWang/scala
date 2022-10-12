package com.aidokay.retirement.retcalc

import cats.effect.{IO, IOApp}
import com.aidokay.retirement.csv.Decoder.RowDecoder
import com.aidokay.retirement.loader.DataLoader
import com.aidokay.retirement.retcalc.Models.EquityData
import cats.effect.IO.asyncForIO
import scala.deriving.Mirror.ProductOf

object RetCalcMain extends IOApp.Simple {
  import com.aidokay.retirement.csv.Decoder.*
  //using cats.IO
  def loadWithIO[P](resource: String)(using p: ProductOf[P], d: RowDecoder[p.MirroredElemTypes]): IO[Vector[P]] =
    for {
      vec <- DataLoader.loadWithIO(resource).use(DataLoader.mapThenSplit())
    } yield vec.map(_.as[P])

  override def run: IO[Unit] =
    for {
      vec <- loadWithIO[EquityData]("sp500.tsv")
      _ <- IO.println(vec.mkString("\n"))
    } yield ()
}
