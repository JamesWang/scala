package com.aidokay.retirement.loader

import cats.data.Reader
import cats.effect.{IO, Resource, Sync}
import com.aidokay.retirement.csv.Decoder.*

import scala.deriving.Mirror.ProductOf
import scala.io.{BufferedSource, Source}
import scala.util.Using

object DataLoader {
  private def mapSplitApply[T](data: Iterator[String])(f: String => T): Vector[T] =
    data.drop(1)
      .withFilter(_.trim.nonEmpty)
      .map(f)
      .toVector

  def sSplit(line: String, delim: String="\\t"): List[String] =
    line.split(delim).toList

  def mapSplit: BufferedSource => Vector[List[String]] = rs => mapSplitApply[List[String]](rs.getLines())(sSplit(_))
  def mapThenSplit[F[_]: Sync]: BufferedSource => F[Vector[List[String]]] = rs => Sync[F].pure(mapSplit(rs))

  def load: Reader[String, Vector[List[String]]] = Reader{rs =>
    Using.resource(Source.fromResource(rs))(mapSplit)
  }

  def loadWithIO1(resource: String): IO[Vector[List[String]]] =
    Resource.fromAutoCloseable(
      IO {Source.fromResource(resource)}
    ).use {
      rs => IO{mapSplit(rs)}
    }

  def loadWithIO[F[_]: Sync](resource: String): Resource[F, BufferedSource] =
    Resource.make(Sync[F].pure(Source.fromResource(resource)))(
      rs => Sync[F].blocking(rs.close())
    )


  //purely scala
  def loadedAs[T](resource: String, delim: String = "\t")(using p: ProductOf[T], d: RowDecoder[p.MirroredElemTypes]): Vector[T] =
    Using.resource(Source.fromResource(resource)) { rs =>
      mapSplitApply(rs.getLines())(sSplit(_, delim).as[T])
    }


    ///Using Reader monad
  def loadFor[P](using p: ProductOf[P], d: RowDecoder[p.MirroredElemTypes]): Reader[String, Vector[P]] =
    Reader {DataLoader.load.run(_).map(_.as[P])}
}
