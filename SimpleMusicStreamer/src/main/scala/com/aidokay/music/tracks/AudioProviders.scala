package com.aidokay.music.tracks


object AudioProviders {
  implicit val audioProvider: AudioProvider[String] =
    new AudioProvider[String]() {
      override def audios(): List[String] = TracksFinder.map3FileFinder.load("")
    }
}
