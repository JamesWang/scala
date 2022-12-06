package com.aidokay.music

import com.aidokay.music.JokeBox._
import com.aidokay.music.JokeBoxData.JokeBoxContext
import com.aidokay.music.tracks.AudioProvider

class JokeBoxHandler(audioProvider: AudioProvider[String]) {

  private val jokeBoxData = new JokeBoxContext(audioProvider)
  var subscribers: List[Any] = Nil

  private def streamAudioChunk(): Unit = {
    if (jokeBoxData.state() == Paused ) return
    jokeBoxData.currentPlaying match {
      case None if jokeBoxData.isEmpty =>
        jokeBoxData.updateCurrentState(Paused)
      case None =>
        jokeBoxData.playNext()
      case Some(playing) =>
        if (playing.isDone && jokeBoxData.isEmpty) {
          jokeBoxData.stopPlaying()
        }else {
          playing.streamAudioChunk(subscribers)
          if (playing.isDone) {
            jokeBoxData.stopPlaying()
          }
        }
    }
  }

  def play(): Unit = {
    jokeBoxData.updateCurrentState(Playing)
  }
  def pause(): Unit = {
    jokeBoxData.updateCurrentState(Paused)
  }

  def list[T](replyTo: T): Unit = {
    //replyTo ! ListedMusic(audioProvider.audioList())
  }

  def schedule(tracks: List[String]): Unit = {
    if (tracks.contains("all")) {
      jokeBoxData.allAudios()
    } else {
      tracks.foreach(jokeBoxData.offer)
    }
  }

  def apply(): Unit = {
    var streamerInstance = None
  }
}
