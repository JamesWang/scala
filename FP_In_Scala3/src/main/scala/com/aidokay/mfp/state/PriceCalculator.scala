package com.aidokay.mfp.state

import com.aidokay.mfp.state.StateM.State

object PriceCalculator {

  object Stubs {
    def findTheBasePrice(productId: String) = 10.0

    def findStateSpecificDiscount(productId: String, stateCode: String) = 0.5

    def findProductSpecificDiscount(productId: String) = 0.5

    def calculateTax(productId: String, price: Double) = 5.0
  }

  case class PriceState(productId: String, stateCode: String, price: Double)

  def findBasePrice(ps: PriceState): Double = Stubs.findTheBasePrice(ps.productId)

  def applyStateSpecificDiscount(ps: PriceState): Double = Stubs.findStateSpecificDiscount(ps.productId, ps.stateCode)

  def applyProductSpecificDiscount(ps: PriceState): Double = Stubs.findProductSpecificDiscount(ps.productId)

  def applyTax(ps: PriceState): Double = Stubs.calculateTax(ps.productId, ps.price)

  def calculatePrice(productId: String, stateCode: String): Double = {
    def modifyPriceState(f: PriceState => Double) = State.modify[PriceState](s => s.copy(price = f(s)))

    val stateM = for {
      _ <- modifyPriceState(findBasePrice)
      _ <- modifyPriceState(applyStateSpecificDiscount)
      _ <- modifyPriceState(applyProductSpecificDiscount)
      _ <- modifyPriceState(applyTax)
    } yield ()
    val initialPriceState = PriceState(productId, stateCode, 0.0)
    val finalPriceState = stateM.apply(initialPriceState)._1
    val finalPrice = finalPriceState.price
    finalPrice
  }
}
