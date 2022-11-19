package com.aidokay.music

import akka.NotUsed
import akka.actor.typed.{ActorRef, ActorSystem}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpMethods.GET
import akka.http.scaladsl.model.MediaType.Compressible
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.scaladsl.Source
import akka.util.{BoundedBlockingQueue, ByteString}
import com.aidokay.music.JokeBox.{Listener, MusicBox, SubscribeMusic}

import java.util.concurrent.ArrayBlockingQueue
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

class StreamingRoutes(musicSubscriber: ActorRef[MusicBox])(implicit val system: ActorSystem[_]) {


  case class MListener() extends Listener {
    override type O = Unit
    val buffer: BoundedBlockingQueue[ByteString] =
      new BoundedBlockingQueue[ByteString](1024, new ArrayBlockingQueue(1024))

    override def listen(chunk: Array[Byte]): Unit = {
      buffer.offer(ByteString(chunk))
    }
  }

  def generateMusicSource(): Source[ByteString, NotUsed] = {
    val listener = MListener()
    musicSubscriber ! SubscribeMusic(listener)
    Source.fromIterator(() => {
      Iterator.continually(listener.buffer.take())
    })
  }
  val mp3: ContentType.Binary = ContentType(
    MediaType.audio("mpeg", comp = Compressible, fileExtensions = "mp3")
  )

  val streamRoutes: Route = pathPrefix("music") {
    concat(
      pathEnd {
        concat(
          get {
            complete { HttpEntity(mp3, generateMusicSource()) }
          }
        )
      }
    )
  }

  val requestHandler: (Http.IncomingConnection, HttpRequest) => HttpResponse = { (conn, request) =>
    request match {
      case HttpRequest(GET, Uri.Path("/music"), _, _, _) =>
        val entity = HttpEntity(mp3, generateMusicSource())
        val response = HttpResponse(
          entity = entity
        )
        response
    }
  }
}
