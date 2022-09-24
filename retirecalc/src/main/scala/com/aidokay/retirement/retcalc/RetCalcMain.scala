package com.aidokay.retirement.retcalc

import cats.effect.{IO, IOApp}
import com.aidokay.retirement.csv.Decoder.RowDecoder

import scala.deriving.Mirror.ProductOf

object RetCalcMain extends IOApp.Simple {
  import Models.*
  //using cats.IO
  def loadWithIO[P](resource: String)(using p: ProductOf[P], d: RowDecoder[p.MirroredElemTypes]): IO[Vector[P]] =
    for {
      vec <- DataLoader.loadWithIO(resource)
    } yield vec.map(_.as[P])

  override def run: IO[Unit] =
    for {
      vec <- loadWithIO[EquityData]("sp500.tsv")
      _ <- IO.println(vec.mkString("\n"))
    } yield ()
}
