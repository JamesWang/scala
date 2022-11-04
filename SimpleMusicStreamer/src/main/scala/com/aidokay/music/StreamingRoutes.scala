package com.aidokay.music

import akka.NotUsed
import akka.actor.typed.{ActorRef, ActorSystem}
import akka.http.scaladsl.model.MediaType.Compressible
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.scaladsl.Source
import akka.util.{BoundedBlockingQueue, ByteString, Timeout}
import com.aidokay.music.JokeBox.Listener
import com.aidokay.music.MusicSubscriber.{ListenMusic, StartPlayMusic}

import java.util.concurrent.ArrayBlockingQueue

class StreamingRoutes(musicSubscriber: ActorRef[ListenMusic])(implicit
    val system: ActorSystem[_]
) {
  private implicit val timeout: Timeout = Timeout.create(
    system.settings.config.getDuration("music-streamer.routes.ask-timeout")
  )

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
    musicSubscriber ! StartPlayMusic(listener)
    Source.fromIterator(() => {
      Iterator.continually(listener.buffer.take())
    })
  }

  val streamRoutes: Route = pathPrefix("music") {
    concat(
      pathEnd {
        concat(
          get {
            complete {
              HttpEntity(
                ContentType(
                  MediaType
                    .audio("mpeg", comp = Compressible, fileExtensions = "mp3")
                ),
                generateMusicSource()
              )
            }
          }
        )
      }
    )
  }
}
