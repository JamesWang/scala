package com.aidokay.contract.domain

import com.aidokay.contract.domain.model.{Invoice, Payment}
import com.aidokay.contract.domain.payment.PaymentPrinting.PrintPayment
import com.aidokay.util.FileUtils.getListOfFiles

object PdfTextExtractorMain extends App {
  val path = "C:\\tmp\\payments\\"

  val pdfFilenames: List[String] =
    getListOfFiles(path, "Contractor_ABC[0-9]+.pdf").map(fn ⇒ path + fn)


  val invoiceFiles = Seq(
    path + "ABC-20210220_20210226.pdf"
  )

  def getPaymentData(files: Seq[String]): Seq[Payment] = {
    import com.aidokay.contract.domain.payment.PaymentParser
    PaymentParser.from2(files)
    //PaymentParser.from3(files)
  }

  def getInvoiceData(files: Seq[String]): Seq[Invoice] = {
    import com.aidokay.contract.domain.invoice.InvoiceParser
    InvoiceParser.from(files)
  }

  val allPayments = getPaymentData(pdfFilenames)
  allPayments.printAll()

}
