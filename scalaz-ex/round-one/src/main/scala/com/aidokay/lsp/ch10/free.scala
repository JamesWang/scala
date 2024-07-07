package com.aidokay.lsp.ch10

import scala.annotation.tailrec

object free {
  //                                         here action: Action[A=Free[_, _]], so bait => Free[_,_]
  def buyBait(name: String): Free[Action, Bait] = Join(BuyBait(name, Done(_)))

  def castLine(bait: Bait): Free[Action, Line] = Join(CastLine(bait, Done(_)))

  def hookFish(line: Line): Free[Action, Fish] = Join(HookFish(line, Done(_)))


  def catchFish(baitName: String): Free[Action, Fish] = for {
    bait <- buyBait(baitName)
    line <- castLine(bait)
    fish <- hookFish(line)
  } yield fish

  @tailrec
  def goFishingAcc[A](actions: Free[Action, A], log: List[AnyVal]): List[AnyVal] = actions match {
    case Join(BuyBait(name, f)) =>
      val bait = Bait(name)
      goFishingAcc(f(bait), bait :: log)
    case Join(CastLine(bait, f)) =>
      val line = Line(bait.name.length)
      goFishingAcc(f(line), line :: log)
    case Join(HookFish(line, f)) =>
      val fish = Fish(s"CatFish from ($line)")
      goFishingAcc(f(fish), fish :: log)
    case Done(_) => log.reverse
  }

  def main(args: Array[String]): Unit = {
    println(goFishingAcc(catchFish("Crankbait"), Nil))
  }
}
