package com.aidokay.music

import akka.util.ByteString
import com.aidokay.music.tracks.{AudioProvider, TrackReader}
import com.aidokay.music.JokeBox._

import scala.collection.mutable

object JokeBoxData {
  class JokeBoxContext(private val audioProvider: AudioProvider[String]) {
    private var currentState: JokeBoxState = Paused
    private val playList: mutable.ArrayDeque[String] =
      new mutable.ArrayDeque[String]()

    def updateCurrentState(state: JokeBoxState): Unit = currentState = state

    def offer(track: String): Unit = playList.append(track)

    def take(): Option[String] =
      if (playList.nonEmpty) Option(playList.remove(0)) else None

    def allAudios(): Unit = {
      playList.addAll(audioProvider.audioList())
      ()
    }

    def state(): JokeBoxState = currentState

    def isEmpty: Boolean = playList.isEmpty

    private var currentPlayingTrack: Option[PlayingTrack] = None

    def currentPlaying: Option[PlayingTrack] = currentPlayingTrack

    def playNext(): Unit = {
      take() match {
        case Some(music) =>
          currentPlayingTrack = Some(
            new PlayingTrack(music, audioProvider.location)
          )
          currentState = Playing
        case _ =>
      }
    }

    def stopPlaying(): Unit = {
      currentPlayingTrack = None
    }

  }

  class PlayingTrack(track: String, location: String) extends AutoCloseable {
    private var done = false
    val trackReader: Iterator[Array[Byte]] = new TrackReader(location = location, music = track).asIterator()

    def streamAudioChunk(): Iterator[ByteString] = {
      trackReader.map(ByteString(_))
    }

    override def close(): Unit = {
      done = true
    }

    def isDone: Boolean = done
  }
}
