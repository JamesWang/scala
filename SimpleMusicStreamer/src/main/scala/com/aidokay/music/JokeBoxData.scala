package com.aidokay.music

import com.aidokay.music.JokeBox.{JokeBoxState, Paused}
import com.aidokay.music.tracks.AudioProvider

import scala.collection.mutable

class JokeBoxData[T](private val audioProvider: AudioProvider[T]) {
  private var currentState: JokeBoxState = Paused
  private val playList: mutable.ArrayDeque[T] = new mutable.ArrayDeque[T]()

  def updateCurrentState(state: JokeBoxState): Unit = currentState = state

  def offer(track: T): Unit = playList.append(track)
  def take(): Option[T] =
    if (playList.nonEmpty) Option(playList.remove(0)) else None
  def allAudios() = {
    playList.addAll(audioProvider.audios())
  }

}
