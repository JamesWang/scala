package com.aidokay.retirement.loader

import cats.data.Reader
import cats.effect.{IO, Resource}
import com.aidokay.retirement.csv.Decoder.RowDecoder

import scala.deriving.Mirror.ProductOf
import scala.io.Source
import scala.util.Using

object DataLoader {
  import com.aidokay.retirement.csv.Decoder.*
  private def mapSplitApply[T](data: Iterator[String])(f: String => T): Vector[T] =
    data.drop(1)
      .withFilter(_.trim.nonEmpty)
      .map(f)
      .toVector

  def sSplit(line: String, delim: String="\\t"): List[String] =
    line.split(delim).toList

  def load: Reader[String, Vector[List[String]]] = Reader(resource =>
    Using.resource(Source.fromResource(resource)) {rs => mapSplitApply[List[String]](rs.getLines())(sSplit(_))})

  def loadWithIO(resource: String): IO[Vector[List[String]]] =
    Resource.fromAutoCloseable(IO {
      Source.fromResource(resource)
    }).use { source =>IO {mapSplitApply[List[String]](source.getLines())(sSplit(_))}}

  //purely scala
  def loadedAs[T](resource: String, delim: String = "\t")(using p: ProductOf[T], d: RowDecoder[p.MirroredElemTypes]): Vector[T] =
    Using.resource(Source.fromResource(resource)) { rs =>
      mapSplitApply(rs.getLines())(sSplit(_, delim).as[T])
    }.toVector


    ///Using Reader monad
  def loadFor[P](using p: ProductOf[P], d: RowDecoder[p.MirroredElemTypes]): Reader[String, Vector[P]] = Reader { resource =>
    DataLoader.load.run(resource).map(_.as[P])
  }

}
