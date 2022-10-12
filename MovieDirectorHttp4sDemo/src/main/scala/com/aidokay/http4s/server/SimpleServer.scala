package com.aidokay.music.server

import cats.effect.{ExitCode, IO, IOApp}
import com.aidokay.music.model.{Director, Movie}
import com.aidokay.music.repo.Repos
import com.aidokay.music.repo.Repos.Repository
import com.aidokay.music.server.Routes.{directorRoutes, movieRoutes}
import com.aidokay.music.service.{MDService, MovieService}
import com.comcast.ip4s.Port
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.Router
import org.http4s.syntax.*

object SimpleServer extends IOApp {

  class MDServiceImpl[F[_], T](using repo: Repository[F, String, T])
      extends MDService[F, T] {
    override val movieService: MovieService[F, T] = new MovieService {}
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
