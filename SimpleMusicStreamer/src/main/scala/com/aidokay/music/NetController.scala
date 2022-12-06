package com.aidokay.music

import com.aidokay.music.JokeBox.{Paused, Playing}
import com.aidokay.music.JokeBoxData.JokeBoxContext
import com.aidokay.music.tracks.{AudioProvider, MusicProviders}
import zio.ZIOAppDefault
import zio.http.model.Method
import zio.http.*

class NetController(audioProvider: AudioProvider[String]) {

  private val jokeBoxData = new JokeBoxContext(audioProvider)

  def list(): List[String] = audioProvider.audioList

  def play(): Unit = {
    jokeBoxData.updateCurrentState(Playing)
  }

  def pause(): Unit = {
    jokeBoxData.updateCurrentState(Paused)
  }

  def schedule(csvTracks: String): Unit = {
    if (csvTracks.contains("all")) {
      jokeBoxData.allAudios()
    } else {
      asList(csvTracks).foreach(jokeBoxData.offer)
    }
  }
  def asList(str: String): List[String] =
    str.substring(10).split(",").toList

}
