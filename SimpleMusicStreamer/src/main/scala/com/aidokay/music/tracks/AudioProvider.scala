package com.aidokay.music.tracks

trait AudioProvider[T] {
  def audios(): List[T]
}
