package com.aten.scala.ps.typeclass

object PayrollSystemWithTypeClassExtension {
  import PayrollSystemWithTypeClass._
  case class JapanPayroll[A]( payees:Vector[A])( implicit processor:PayrollProcessor[JapanPayroll,A]) {
    def processPayroll = processor.processPayroll( payees )
  }
  case class Contractor( name:String )
}

object PayrollProcessorsExtension {
  
  import PayrollSystemWithTypeClassExtension._
  import PayrollSystemWithTypeClass._
  
  implicit object JapanPayrollProcessor extends PayrollProcessor[JapanPayroll,Employee] {
    def processPayroll( payees:Seq[Employee]) = 
      Left(" Japan employees are processed")
  }
  
  implicit object USContractorProcessor extends PayrollProcessor[USPayroll,Contractor] {
    def processPayroll( payees:Seq[Contractor]) = 
      Left(" US contractors are processed")
  }
  
  implicit object CanadaContractorProcessor extends PayrollProcessor[CanadaPayroll,Contractor] {
    def processPayroll( payees:Seq[Contractor]) = 
      Left("Canada contractors are processed")
  }
  implicit object JapanContractorProcessor extends PayrollProcessor[JapanPayroll,Contractor] {
    def processPayroll( payees:Seq[Contractor]) = 
      Left(" japen contractors are processed")
  }
}

object RunNewPayroll extends App {
  import PayrollSystemWithTypeClass._
  import PayrollProcessors._
  import PayrollSystemWithTypeClassExtension._
  import PayrollProcessorsExtension._
  //implicit process will be injected by compiler, in this case it is JapanPayrollProcessor
  val r1 = JapanPayroll( Vector(Employee("a",1))).processPayroll   
  val r2 = JapanPayroll( Vector(Contractor("a"))).processPayroll
  
  println(s"r1=$r1")
  println(s"r2=$r2")
}