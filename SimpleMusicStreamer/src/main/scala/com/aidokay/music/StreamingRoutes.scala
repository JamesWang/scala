package com.aidokay.music

import akka.actor.typed.scaladsl.AskPattern.{Askable, schedulerFromActorSystem}
import akka.actor.typed.{ActorRef, ActorSystem}
import akka.http.scaladsl.model.MediaType.Compressible
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import com.aidokay.music.JokeBox.{MusicBox, SubscribeMusic, Subscribed}

import scala.concurrent.Future

class StreamingRoutes(musicSubscriber: ActorRef[MusicBox])(implicit val system: ActorSystem[_]
) {
  private implicit val timeout: Timeout = Timeout.create(
    system.settings.config.getDuration("music-streamer.routes.ask-timeout")
  )

  def generateMusicSource(): Future[Subscribed]= {
    musicSubscriber.ask(SubscribeMusic)
  }

  val streamRoutes: Route = pathPrefix("music") {
    concat(
      pathEnd {
        concat(
          get {
            onSuccess (generateMusicSource()) { result =>
              complete (
                HttpEntity(
                  ContentType(
                    MediaType
                      .audio("mpeg", comp = Compressible, fileExtensions = "mp3")
                  ),
                  result.musicSource.map(next=>next())
                )
            )}
          }
        )
      }
    )
  }
}
