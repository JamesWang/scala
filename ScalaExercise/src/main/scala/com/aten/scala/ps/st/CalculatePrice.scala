package com.aten.scala.ps.st

object TaxCalculation {
  
  def calculatePrice( product: String, taxingStrategy: String => Double) ={
    val tax = taxingStrategy(product)
    println( s" Tax is $tax" ) 
    tax
  }
  
  trait TaxStrategy {
    def taxIt( product: String ) : Double
  }
  class ATaxStrategy extends TaxStrategy {
    def taxIt( product: String ) : Double = 120.0
  }
  
  class BTaxStrategy extends TaxStrategy {
    def taxIt( product: String ) : Double =  220.0
  }
  def taxIt : TaxStrategy=>String=>Double = s=>p=>s.taxIt(p)
  
  def taxItA : String=>Double = taxIt(new ATaxStrategy )
  def taxItB : String=>Double = taxIt(new BTaxStrategy )
}

object CalculatePrice extends App {
  import TaxCalculation._
  
  println( "strategyA->" + taxItA("abc") )
  println( "strategyB->" + taxItB("abc") )
}