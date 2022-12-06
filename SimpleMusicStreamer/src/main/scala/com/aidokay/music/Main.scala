package com.aidokay.music

import com.aidokay.music.tracks.MusicProviders
import zio.ZIOAppDefault
import zio.http.*
import zio.http.model.Method
import zio.*

object Main extends ZIOAppDefault {

  private val port = 8088
  private val netController = new NetController(
    MusicProviders.mp3Provider("V:\\MusicPhotos\\music\\")
  )

  private val admin: String = "admin"
  private val adminRoutes: HttpApp[Any, Nothing] = Http.collect[Request] {
    case Method.GET -> !! / admin / "list" =>
      Response.text(netController.list().mkString("\n"))
    case Method.GET -> !! / admin / "schedule" / music =>
      netController.schedule(music); Response.ok
    case Method.GET -> !! / admin / "play" => netController.play(); Response.ok
    case Method.GET -> !! / admin / "pause" =>
      netController.pause(); Response.ok
  }

  private val playMusic: HttpApp[Any, Nothing] = Http.collectZIO[Request] {
    case Method.GET -> !! / "music" => ???
  }
  val config: ServerConfig = ServerConfig.default.port(port)
  val run =
    Server.serve(adminRoutes).provide(ServerConfig.live(config), Server.live)
}
