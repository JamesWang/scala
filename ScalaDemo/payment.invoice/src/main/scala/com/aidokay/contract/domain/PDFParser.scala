package com.aidokay.contract.domain

import com.aidokay.contract.domain.PdfDataLoader.{DataLoader, DataLoader2, PDFFile}
import com.aidokay.contract.domain.model.{Convertible, PdfValue}
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper

import scala.concurrent.{blocking, Await, Future, Promise}
import scala.concurrent.duration.Duration
import scala.language.implicitConversions
import scala.util.{Try, Using}

trait PDFParser[T, A <: Convertible] {
  def sourceValues(): Map[T, Int]

  def mapping(data: Map[String, PdfValue]): A

  def skip: Int = 0

  def preprocessData(data: String, skip: Int): Seq[(String, Int)] = {
    data.split("\\n")
      .drop(skip)
      .filter(x => x.nonEmpty)
      .zipWithIndex
      .filter(v => sourceValues().values.toSet.contains(v._2))
      .map(x => (x._1.stripLineEnd, x._2))
      .toSeq
  }

  def lookupData(lookup: Lookup[T], dataMap: Map[Int, String]): (String, PdfValue) = {
    val v = if (lookup.getRange._2 < 0) dataMap(lookup.getIndex).substring(lookup.getRange._1)
    else dataMap(lookup.getIndex).substring(lookup.getRange._1, lookup.getRange._2)
    lookup.getLabel -> PdfValue(lookup.getLabel, v)
  }

  implicit def valueToLookup(x: T): Lookup[T] = x.asInstanceOf[Lookup[T]]

  def createPdfValue(data: Seq[(String, Int)]): Map[String, PdfValue] = {
    val dataMap = data.map(tp => tp._2 -> tp._1).toMap
    sourceValues().keys.toSeq.map(lk => lookupData(lk, dataMap)).toMap
  }

  //Converter: Seq[PDDocument] => Seq[Convertible]
  def converter(): Converter[Seq[PDDocument]] = (pds: Seq[PDDocument]) => pds.map { x => converter2(x) }


  import scala.concurrent.ExecutionContext.Implicits._

  def converter2: PDDocument => A = (pds: PDDocument) =>
    Using.resource(pds) { pdd =>
      val text: String = new PDFTextStripper().getText(pdd)
      println(text)
      val dataMap = preprocessData(text, this.skip)
      val data = createPdfValue(dataMap)
      mapping(data)
    }


  def from(files: Seq[String])(implicit dataLoader: DataLoader[Future[PDDocument]]): Seq[A] = {
    //converter().convert(dataLoader.loadData(files)).asInstanceOf[Seq[A]]
    val resultF = dataLoader.loadData(files).map(f => f.map(r => converter2(r)))
    resultF.map(f => Await.result(f, Duration.Inf))
  }

  def applyAsync[X](f: => X)(ioBlocking: Boolean = false): Future[X] = {
    Future {
      if (ioBlocking) blocking {
        f
      } else f
    }
  }

  implicit class FutureOps(val self: Future[PDDocument]) {
    def -->(that: PDDocument => A): Future[A] = {
      val p = Promise[A]
      //self onComplete { r => p.tryComplete(Try(that(r.get))) }
      self map { r => p.tryComplete(Try(that(r))) }
      p.future
    }
  }

  def from2(files: Seq[String])(implicit dataLoader: DataLoader2[PDDocument]): Seq[A] = {
    files
      .map(file => applyAsync(dataLoader.loadData(file))(ioBlocking = true))
      .map(f => f.flatMap(r => applyAsync(converter2(r))()))
      .map(f => Await.result(f, Duration.Inf))
  }

  import PdfDataLoader.PDFFileLoader

  def from3(files: Seq[String]): Seq[A] = {
    files
      .map(PDFFile)
      .map(file => applyAsync(file.load)(ioBlocking = true))
      .map(_ --> converter2)
      //.map(f => f.map(r=> converter2(r)))
      //.map(f => Await.result(f, FiniteDuration(10, SECONDS)))
      .map(f => Await.result(f, Duration.Inf))
  }
}

