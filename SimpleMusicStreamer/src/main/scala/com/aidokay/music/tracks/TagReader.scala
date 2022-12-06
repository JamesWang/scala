package com.aidokay.music.tracks

import org.apache.tika.metadata.Metadata
import org.apache.tika.parser.ParseContext
import org.apache.tika.parser.mp3.Mp3Parser
import org.xml.sax.ContentHandler
import org.xml.sax.helpers.DefaultHandler

import java.io.{BufferedInputStream, File, FileInputStream, InputStream}
import scala.annotation.targetName
import scala.jdk.CollectionConverters.*
import scala.util.Using

object TagReader {

  case class SongInfo(title: String, artist: String, contentType: String)

  private def doParse(metadata: Metadata, resource: InputStream): Metadata = {
    new Mp3Parser().parse(
      resource,
      new DefaultHandler(),
      metadata,
      new ParseContext()
    )
    metadata
  }

  private def displayInfo(song: String, metadata: Metadata): Unit = {
    // Retrieve the necessary info from metadata
    // Names - title, xmpDM:artist etc. - mentioned below may differ based
    println(s"--------------------$song--------------------------")

    println("Title: " + metadata.get("dc:title"))
    println("Artists: " + metadata.get("xmpDM:artist"))
    println("Content-Type: " + metadata.get("Content-Type"))
  }
  def songInfo(song: String): SongInfo = {
    Using.resource(new FileInputStream(new File(song))) { res =>
      val metadata = doParse(new Metadata(), res)
      displayInfo(song, metadata)
      SongInfo(
        title = metadata.get("title"),
        contentType = metadata.get("Content-Type"),
        artist = metadata.get("xmpDM:artist")
      )
    }
  }

  def main(args: Array[String]): Unit = {
    val loc = "V:\\MusicPhotos\\music\\"
    MusicProviders
      .mp3Provider(loc)
      .audioList
      .map(x => loc + x)
      .foreach(songInfo)
  }
}
