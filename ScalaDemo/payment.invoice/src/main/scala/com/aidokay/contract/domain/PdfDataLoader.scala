package com.aidokay.contract.domain

import org.apache.pdfbox.io.RandomAccessBufferedFileInputStream
import org.apache.pdfbox.pdfparser.PDFParser
import org.apache.pdfbox.pdmodel.PDDocument

import scala.concurrent.{blocking, Future}
import scala.util.Using


object PdfDataLoader {

  trait DataLoader[A] {
    def loadData(a: Seq[String]): Seq[A]
  }

  trait DataLoader2[A] {
    def loadData(a: String): A
  }

  import scala.concurrent.ExecutionContext.Implicits._

  implicit def loadPDFData: DataLoader[Future[PDDocument]] = (files: Seq[String]) ⇒
    files.map { file ⇒
      Future {
        blocking {
          val pdfDoc = Using.resource(new RandomAccessBufferedFileInputStream(file)) {
            rs ⇒
              val parser = new PDFParser(rs)
              parser.parse()
              val cosDoc = parser.getDocument
              new PDDocument(cosDoc)
          }
          pdfDoc
        }
      }
    }

  implicit def loadPDFData2: DataLoader2[PDDocument] = (file: String) ⇒ {
    val pdfDoc = Using.resource(new RandomAccessBufferedFileInputStream(file)) {
      rs ⇒
        val parser = new PDFParser(rs)
        parser.parse()
        val cosDoc = parser.getDocument
        new PDDocument(cosDoc)
    }
    pdfDoc
  }
  case class PDFFile(file: String)
  implicit class PDFFileLoader(pdfFile: PDFFile) {
    def load: PDDocument = {
      val pdfDoc = Using.resource(new RandomAccessBufferedFileInputStream(pdfFile.file)) {
        rs ⇒
          val parser = new PDFParser(rs)
          parser.parse()
          val cosDoc = parser.getDocument
          new PDDocument(cosDoc)
      }
      pdfDoc
    }
  }
}
