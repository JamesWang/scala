package com.aidokay.music.tracks

import java.io.{BufferedInputStream, FileInputStream}
import java.nio.file.Paths
import scala.util.{Failure, Success, Try}

class TrackReader(location: String, music: String) {

  def asIterator(): Iterator[Array[Byte]] = new Iterator[Array[Byte]] {

    var eof: Boolean = false
    override def hasNext: Boolean = !eof
    val bis = new BufferedInputStream(
      new FileInputStream(Paths.get(location, music).toFile)
    )

    var buff: Seq[Array[Byte]] = LazyList
      .continually {
        val ba: Array[Byte] = new Array[Byte](4096 * 4)
        bis.read(ba)
        ba
      }
      .takeWhile(_.length > 0)

    private def chunk(): Array[Byte] = {
      val head = buff.head
      buff = buff.tail
      head
    }
    private def cpmplete(): Array[Byte] = {
      eof = true
      Array.empty[Byte]
    }
    private def closeIfDone(): Unit = {
      if (eof) bis.close()
    }
    override def next(): Array[Byte] = {
      val result = Try {
        if (buff.head.length > 0) chunk() else cpmplete()
      } match {
        case Success(value) => value
        case Failure(_) =>  cpmplete()
      }
      closeIfDone()
      result
    }
  }
}
