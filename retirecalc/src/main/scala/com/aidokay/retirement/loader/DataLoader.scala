package com.aidokay.retirement.loader

import cats.data.Reader
import cats.effect.{IO, Resource}
import com.aidokay.retirement.csv.Decoder.RowDecoder

import scala.deriving.Mirror.ProductOf
import scala.io.{BufferedSource, Source}
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

  private def mapSplit = (rs: BufferedSource) => mapSplitApply[List[String]](rs.getLines())(sSplit(_))

  def load: Reader[String, Vector[List[String]]] = Reader{rs =>
    Using.resource(Source.fromResource(rs))(mapSplit)
  }

  def loadWithIO(resource: String): IO[Vector[List[String]]] =
    Resource.fromAutoCloseable(
      IO {Source.fromResource(resource)}
    ).use {
      rs => IO{mapSplit(rs)}
    }

  //purely scala
  def loadedAs[T](resource: String, delim: String = "\t")(using p: ProductOf[T], d: RowDecoder[p.MirroredElemTypes]): Vector[T] =
    Using.resource(Source.fromResource(resource)) { rs =>
      mapSplitApply(rs.getLines())(sSplit(_, delim).as[T])
    }


    ///Using Reader monad
  def loadFor[P](using p: ProductOf[P], d: RowDecoder[p.MirroredElemTypes]): Reader[String, Vector[P]] =
    Reader {DataLoader.load.run(_).map(_.as[P])}
}
