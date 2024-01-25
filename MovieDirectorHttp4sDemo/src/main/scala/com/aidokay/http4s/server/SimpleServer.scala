package com.aidokay.http4s.server

import cats.effect.{ExitCode, IO, IOApp}
import com.aidokay.http4s.model.{Director, Movie}
import com.aidokay.http4s.repo.Repos
import com.aidokay.http4s.repo.Repos.Repository
import com.aidokay.http4s.server.Routes.{directorRoutes, movieRoutes}
import com.aidokay.http4s.service.{MDService, MovieService}
import com.comcast.ip4s.Port
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.Router
import org.http4s.syntax.*

object SimpleServer extends IOApp {

  private class MDServiceImpl[F[_], T](using repo: Repository[F, String, T])
      extends MDService[F, T]
      with MovieService[F, T] {
    override val repository: Repository[F, String, T] = repo
  }

  override def run(args: List[String]): IO[ExitCode] =
    val apis = Router(
      "/api" -> movieRoutes[IO].run(
        new MDServiceImpl[List, Movie](using Repos.movieRepo)
      ),
      "/api/private" -> directorRoutes[IO].run(
        new MDServiceImpl[Option, Director](using Repos.directorRepo)
      )
    ).orNotFound

    println("starting http server")

    EmberServerBuilder
      .default[IO]
      .withPort(Port.fromInt(8088).get)
      .withHttpApp(apis)
      .build
      .use(_ => IO.never)
      .as(ExitCode.Success)

}
