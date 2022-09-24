package com.aidokay.retirement.retcalc

import cats.data.Reader

import cats.effect.{ IO, Resource}

import com.aidokay.retirement.csv.Decoder.RowDecoder

import scala.deriving.Mirror.ProductOf
import scala.io.Source
import scala.util.{Try, Using}

object DataLoader {
  def load: Reader[String, Vector[List[String]]] = Reader(resource =>
    Using.resource(Source.fromResource(resource)) { r =>
      r.getLines().drop(1).filter(_.trim.nonEmpty).map { line =>
        line.split("\t").toList
      }.toVector
    })

  def loadWithIO(resource: String): IO[Vector[List[String]]] =
    Resource.fromAutoCloseable(IO {Source.fromResource(resource)} ).use { source =>
      IO {
        source.getLines().drop(1).filter(_.trim.nonEmpty).map { line =>
          line.split("\t").toList
        }.toVector
      }
    }
}