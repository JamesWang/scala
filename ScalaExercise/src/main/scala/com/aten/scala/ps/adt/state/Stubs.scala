package com.aten.scala.ps.adt.state

object Stubs {
  def findTheBasePrice( productId:String) : Double = 10.0
  def findStateSpecificDiscount( 
      productId:String, stateCode:String) : Double = 0.5
  def findProductSpecificDiscount( productId: String ) : Double = 0.5
  def calculateTax( productId: String, price:Double ) : Double = 5.0
  
}