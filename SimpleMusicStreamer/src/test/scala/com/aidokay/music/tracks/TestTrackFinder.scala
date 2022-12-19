package com.aidokay.music.tracks

import org.scalactic.TypeCheckedTripleEquals
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class TestTrackFinder
    extends AnyWordSpec
    with Matchers
    with TypeCheckedTripleEquals {

  "TrackFileLoader.load()" should {
    "get a list of mp3 files" in {
      val result = TracksFinder.map3FileFinder.load("V:\\MusicPhotos\\music")
      result.size should ===(53)
    }
  }

  "TrackReader.asIterator()" should {
    "works ok" in {
      val iter = new TrackReader("V:\\MusicPhotos\\music", "1902262839.mp3").asIterator()
      for {
        _ <- iter.next()
      } yield ()
    }
  }

}
