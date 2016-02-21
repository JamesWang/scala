package com.aten.scala.ps.st

//PhantomTypes 
sealed trait OrderComplete
sealed trait InCompleteOrder
sealed trait ItemProvided
sealed trait NoItem
sealed trait AddressProvided
sealed trait NoAddress

case class Order[A,B,C]( itemId: Option[String], shippingAddress :Option[String])

object Order {
  def emptyOrder = Order[InCompleteOrder,NoItem,NoAddress]( None,None)
}

object OrderSystem {
  def addItem[A,B]( item:String, o:Order[A,NoItem,B]) = 
    o.copy[A,ItemProvided,B](itemId=Some(item))
  
  def addShipping[A,B](address:String, o:Order[A,B,NoAddress]) = 
    o.copy[A,B,AddressProvided](shippingAddress=Some(address))
 
  def placeOrder( o:Order[InCompleteOrder,ItemProvided,AddressProvided]) =
    o.copy[OrderComplete, ItemProvided,AddressProvided]()
}
object PhantomTypes extends App {
  import OrderSystem._
  
  val o1 = Order.emptyOrder
  
  val o2 = addItem(" Some Book", o1)
  
  val o3 = addShipping("Some address", o2 )
  val o4 = placeOrder( o3 )
  
}