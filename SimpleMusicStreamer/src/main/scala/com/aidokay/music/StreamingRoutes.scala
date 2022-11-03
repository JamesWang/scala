package com.aidokay.music

import akka.actor.typed.scaladsl.AskPattern._
import akka.actor.typed.{ActorRef, ActorSystem}
import akka.http.scaladsl.model.MediaType.Compressible
import akka.http.scaladsl.model.{ContentType, HttpEntity, HttpResponse, MediaType, MediaTypes, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.scaladsl.Source
import akka.util.Timeout
import com.aidokay.music.MusicSubscriber.{ListenMusic, StartPlayMusic}

class StreamingRoutes(musicSubscriber: ActorRef[ListenMusic])(implicit val system: ActorSystem[_]) {
  private implicit val timeout: Timeout = Timeout.create(
    system.settings.config.getDuration("music-streamer.routes.ask-timeout")
  )

  val streamRoutes: Route = pathPrefix("music") {
    concat(
      pathEnd {
        concat(
          get {
            system.log.info("inside get....")
            complete(
              HttpResponse(
                StatusCodes.PartialContent,
                entity = HttpEntity.Chunked.fromData(
                  ContentType(MediaType.audio("mpeg", comp = Compressible, fileExtensions = "mp3")),
                  Source.future(musicSubscriber.ask(StartPlayMusic))
                ))
            )
          }
        )
      }
    )
  }
}
