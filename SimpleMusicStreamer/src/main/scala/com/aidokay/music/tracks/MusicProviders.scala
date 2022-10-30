package com.aidokay.music.tracks


object MusicProviders {
  implicit val audioProvider: AudioProvider[String] =
    new AudioProvider[String]() {
      override def audioList(): List[String] = TracksFinder.map3FileFinder.load("")
    }
}
