package com.aidokay.algegraic

object Fishes {

  final case class Fish(volume: Int, size: Int, teeth: Int, poisonousness: Int)


  given volumeSemigroup: SemigroupEx.Semigroup[Fish] = (l: Fish, r: Fish) => {
    val result = if (l.volume >= r.volume) l else r
    result.copy(volume = r.volume + l.volume)
  }

}
