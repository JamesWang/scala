package com.aidokay.music

import akka.actor.typed.scaladsl.AskPattern.{Askable, schedulerFromActorSystem}
import akka.actor.typed.{ActorRef, ActorSystem}
import akka.http.scaladsl.model.MediaType.Compressible
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.{ContentDispositionTypes, `Content-Disposition`}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.scaladsl.Source
import akka.util.Timeout
import com.aidokay.music.JokeBox.{DownloadInfo, DownloadMusic, MusicBox, SubscribeMusic, Subscribed}

import scala.concurrent.Future

class StreamingRoutes(musicSubscriber: ActorRef[MusicBox])(implicit val system: ActorSystem[_]) {
  private implicit val timeout: Timeout = Timeout.create(
    system.settings.config.getDuration("music-streamer.routes.ask-timeout")
  )

  private def generateMusicSource(): Future[Subscribed] = musicSubscriber.ask(SubscribeMusic)

  private def mp3(subType: String): ContentType.Binary = ContentType(
    MediaType.audio( subType, comp = Compressible, fileExtensions = "mp3")
  )
  def askForTrackLocation(track: String): Future[DownloadInfo] = musicSubscriber.ask(DownloadMusic.apply(track, _))

  val streamRoutes: Route = {
    concat(
      pathPrefix("music") {
        concat(
          pathPrefix("listen") {
            concat(get {
              onSuccess(generateMusicSource()) { result =>
                complete(HttpEntity(mp3("mpeg"), result.musicSource))
              }
            })
          },
          (pathPrefix("download") & parameter("track")) { track =>
            onSuccess(askForTrackLocation(track)) { trackInfo =>
              respondWithHeader(`Content-Disposition`(
                ContentDispositionTypes.attachment, Map("filename" -> trackInfo.trackName)
              )) {
                complete(HttpEntity(mp3("application"), MusicDownloader.trackContentSource(trackInfo.fullPath.toString)))
              }
            }
          }
        )}
    )
  }
}
