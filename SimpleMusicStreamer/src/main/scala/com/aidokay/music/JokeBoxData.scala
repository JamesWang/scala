package com.aidokay.music

import com.aidokay.music.JokeBox.{JokeBoxState, Listener, Paused, Playing}
import com.aidokay.music.tracks.AudioProvider

import java.io.RandomAccessFile
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
      currentPlayingTrack.foreach(_.close())
      currentPlayingTrack = None
    }

  }

  class PlayingTrack(track: String, location: String) extends AutoCloseable {
    private val chunkSize: Int = 4096
    private var done = false

    var currentFile: RandomAccessFile =
      new RandomAccessFile(location + track, "r")
    var positionInFile: Int = 0

    def streamAudioChunk(listeners: List[Listener]): Unit = {
      try {
        if (!done) {
          val dataArray = Array.ofDim[Byte](chunkSize)
          currentFile.seek(positionInFile)
          val byteRead = currentFile.read(dataArray, 0, chunkSize)
          if (byteRead > 0) {
            positionInFile += byteRead
            listeners.foreach(_.listen(dataArray))
          } else {
            close()
          }
        }
      } catch {
        case e: Throwable =>
          e.printStackTrace()
          close()
      }
    }

    override def close(): Unit = {
      currentFile.close()
      done = true
    }

    def isDone: Boolean = done
  }
}
