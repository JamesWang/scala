package com.aten.scala.ps.adt.state

object PriceCalculator {
  import Stubs._
  import StateMonad._
  import State._

  case class PriceState( productId: String, stateCode:String, price:Double)
  
  def findBasePrice( ps: PriceState ): Double = findTheBasePrice( ps.productId )    
  def applyStateSpecificDiscount( ps: PriceState ): Double = 
    ps.price - findStateSpecificDiscount(ps.productId, ps.stateCode )
  
  def applyProductSpecificDiscount( ps: PriceState ): Double = 
    ps.price - findProductSpecificDiscount(ps.productId)
    
  def applyTax(ps:PriceState ): Double = ps.price + calculateTax(ps.productId, ps.price )

  def calculatePrice( productId:String, stateCode:String ) : Double = {
    // def modify[S](f: S => S) : State[S,Unit] = init[S] flatMap (s => state(_ => (f(s), ())))
    // def init[S]: State[S, S] = state[S, S](s => (s, s))
    // def state[S, A](f: S => (S, A)) : State[S,A] = new State[S, A] {
    //   def apply(s: S) = f(s)
    // }
    def modifyPriceState( f: PriceState => Double ) :State[PriceState,Unit] =   
      modify[PriceState]( s=>s.copy( price = f(s) ) )
      //=state(s=>(s,s)) flatMap ( s=>state(_=>(f(s),())))
      //=State {
      // def apply( s: S ) = f(s)
      //"
    val stateMonad : State[PriceState,Unit] = for {
      _ <- modifyPriceState( findBasePrice ) 
      _ <- modifyPriceState( applyStateSpecificDiscount )
      _ <- modifyPriceState( applyProductSpecificDiscount )
      _ <- modifyPriceState( applyTax )
    } yield ()
    
    val stateMonad1 = modifyPriceState(findBasePrice) flatMap( a=>
      modifyPriceState( applyStateSpecificDiscount ) flatMap( b =>
        modifyPriceState( applyProductSpecificDiscount ) flatMap ( c =>
          modifyPriceState( applyTax ) map ( d=> () )) ) )
    /* modifyPriceState(findBasePrice) == modify[PriceState]( f1: ps: PriceState => ps.copy( price = findBasePrice(ps) ) )
     * 
     * modify[PriceState](f1: PriceState => PriceState ) == 
     *                  init[PriceState] flatMap (ps: PriceState => state( ps : PriceState => (f1(ps), ())))
     * 										init[PriceState] == state[PriceState, PriceState](s : PriceState => (PriceState, PriceState))
     * 																					state[PriceState,PriceState] == new State[PriceState,PriceState] {
     * 																							def apply[PriceState]( s: PriceState ) = (PriceState,PriceState)
     * 																					}
     * 
     *   
     *       
     */
    val findBasePriceMonad = new State[PriceState,PriceState] {
      def apply( s: PriceState ) : ( PriceState,PriceState) = ( s, s )      
    } flatMap( ps => state(ps => (ps.copy(price =findBasePrice(ps)),() )))
    
    val applyStateDiscountMonad = new State[PriceState,PriceState] {
      def apply( s: PriceState ) : ( PriceState,PriceState) = ( s, s )      
    } flatMap( ps => state(ps => (ps.copy(price =applyStateSpecificDiscount(ps)),() )))

    val applyProductDiscountMonad = new State[PriceState,PriceState] {
      def apply( s: PriceState ) : ( PriceState,PriceState) = ( s, s )      
    } flatMap( ps => state(ps => (ps.copy(price =applyProductSpecificDiscount(ps)),() )))

    val applyTaxMonad = new State[PriceState,PriceState] {
      def apply( s: PriceState ) : ( PriceState,PriceState) = ( s, s )      
    } flatMap( ps => state(ps => (ps.copy(price =applyTax(ps)),() )))

    val initialPriceState = PriceState(productId, stateCode, 0.0)
    
    println(
        applyTaxMonad.apply(
          applyProductDiscountMonad.apply(
            applyStateDiscountMonad.apply(
              findBasePriceMonad.apply(initialPriceState)._1
            )._1
          )._1
        )._1.price)
    
    println(
      findBasePriceMonad.flatMap(
         _ => applyStateDiscountMonad.flatMap( _ => applyProductDiscountMonad.flatMap(_ => applyTaxMonad))
      ).apply(initialPriceState)._1.price
    )
    val finalPriceState   = stateMonad.apply(initialPriceState)._1
    val finalPrice = finalPriceState.price
    finalPrice
  } 
}
import PriceCalculator._
object RunMe extends App {
   println( calculatePrice("abc","124"))
}