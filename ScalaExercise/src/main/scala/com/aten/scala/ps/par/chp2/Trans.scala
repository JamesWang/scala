package com.aten.scala.ps.par.chp2

/**
 * @author jooly
 */
import aten.util.Utility._
object Trans extends App {
  import scala.collection._
  private val transfers = mutable.ArrayBuffer[String]()
  def logTransfer(name: String, n: Int) =
    transfers.synchronized {
      transfers += s" transfer to account '$name' = $n"
    }
  var uidCount = 0L
  def getUniqueId() = {
    this.synchronized {
      val freshUid = uidCount + 1
      uidCount = freshUid
      freshUid
    }
  }
  class Account(val name: String, var money: Int) { val uid = getUniqueId() }
  def add(account: Account, n: Int) = account.synchronized {
    account.money += n
    if (n > 10) logTransfer(account.name, n)
  }
  def send(a: Account, b: Account, n: Int) =
    a.synchronized {
      b.synchronized {
        a.money -= n
        b.money += n
      }
    }
  val jane = new Account("Jane", 100)
  val john = new Account("John", 200)
  val t1 = thread { add(jane, 5) }
  val t2 = thread { add(john, 50) }
  val t3 = thread { add(jane, 70) }
  t1.join(); t1.join(); t3.join()
  Console.print(s"-----transfers ----\n$transfers")
}