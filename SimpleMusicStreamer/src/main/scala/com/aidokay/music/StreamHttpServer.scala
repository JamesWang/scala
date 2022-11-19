package com.aidokay.music

import akka.actor.typed.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.scaladsl.Sink

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

object StreamHttpServer {
  def startHttpServer(
      routes: StreamingRoutes
  )(implicit system: ActorSystem[_]): Unit = {
    // Akka HTTP still needs a classic ActorSystem to start
    // val futureBinding = Http().newServerAt("0.0.0.0", 8080).bind(routes)
    val serverSource = Http()
      .newServerAt("0.0.0.0", 8080)
      .connectionSource()

    val futureBinding = serverSource
      .to(Sink.foreach { connection =>
        val rHandler = routes.requestHandler(connection, _)
        connection.handleWithSyncHandler(rHandler)
      }).run()

    futureBinding.onComplete {
      case Success(binding) =>
        val address = binding.localAddress
        system.log.info(
          "Server online at http://{}:{}/",
          address.getHostString,
          address.getPort
        )
      case Failure(ex) =>
        system.log.error("Failed to bind HTTP endpoint, terminating system", ex)
        system.terminate()
    }
  }

}
