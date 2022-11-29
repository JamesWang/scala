package com.aidokay.music.tracks

import scala.language.implicitConversions


object MusicProviders {
  def mp3Provider(loc: String): AudioProvider[String] =
    new AudioProvider[String]() {
      override def audioList(): List[String] = TracksFinder.map3FileFinder.load(loc)

      override val location: String = loc
    }
}
