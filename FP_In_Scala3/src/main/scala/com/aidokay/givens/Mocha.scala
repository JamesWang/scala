package com.aidokay.givens

object Mocha {

  class PreferredDrink(val preference: String)
  given pref: PreferredDrink = new PreferredDrink("mocha")

  def enjoy(name: String)(using drink: PreferredDrink): Unit =
    print(s"Welcome, $name")
    print(". Enjoy a ")
    print(drink.preference)
    println("!")

  def callEnjoy(): Unit = enjoy("reader")
}
