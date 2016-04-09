package com.aten.scala.ps.pt

sealed trait PreTaxDeductions
sealed trait PostTaxDeductions
sealed trait Final

case class Employee ( 
    name:String, 
    annualSalary:Float, 
    taxRate:Float, 
    insurancePremiumPerPayPeriod:Float, 
    _401kDeductionRate:Float, 
    postTaxDeductions:Float
)

case class Pay[Step]( employee:Employee, netPay:Float )
object Payroll {
    //biweekly paychecks, assume exactly 52 weeks/year for simplicity
  def start( employee:Employee ) : Pay[PreTaxDeductions] =
    Pay[PreTaxDeductions]( employee, employee.annualSalary / 26.0F)
    
  def minusInsurance( pay:Pay[PreTaxDeductions]) : Pay[PreTaxDeductions] = 
    pay copy( netPay = pay.netPay - pay.employee.insurancePremiumPerPayPeriod)
  
  def minus401k( pay:Pay[PreTaxDeductions]) :Pay[PreTaxDeductions] =
    pay copy( netPay = pay.netPay - pay.employee._401kDeductionRate * pay.netPay)

  def minusTax( pay:Pay[PreTaxDeductions]) : Pay[PostTaxDeductions] =
    pay copy ( netPay = pay.netPay - pay.employee.taxRate * pay.netPay)

  def minusFinal( pay: Pay[PostTaxDeductions]) : Pay[Final] =
    pay copy( netPay = pay.netPay - pay.employee.postTaxDeductions)  
}

object Pipeline {
  import scala.language.implicitConversions
  implicit class ToPiped[V]( value: V) {
    def |>[R]( f: V => R) = f( value )
  }
}
object PhantomTypes {

  def main(args:Array[String]) = {
    val e = Employee("Buck Trends", 100000.0F, 0.25F, 200F, 0.10F, 0.05F)
    //run normal
    run1(e)
    
    //run implicit
    run2(e)
  }

  def print( e:Employee, pay:Pay[Final] ) ={
     val twoWeekGross = e.annualSalary /26.0F
    val twoWeekNet = pay.netPay
    val percent = ( twoWeekNet / twoWeekGross ) * 100
    
    println(s" For ${e.name} , the gross vs. net pay every 2 weeks is:")
    println(f"$$${twoWeekGross}%.2f vs. $$${twoWeekNet}%.2f or ${percent}%.1f%%")
  }
  def run1( e: Employee ) = {
    val pay1 = Payroll start e
    
    val pay2 = Payroll minus401k pay1
    val pay3 = Payroll minusInsurance pay2
    val pay4 = Payroll minusTax pay3
    val pay = Payroll minusFinal pay4
    print(e, pay)
  }
  def run2( e: Employee ) = {
    import Pipeline._
    import Payroll._
    
    val pay = start(e)   |>
        minus401k        |>
        minusInsurance   |>
        minusTax         |>
        minusFinal       
    print( e, pay)    
  }
}