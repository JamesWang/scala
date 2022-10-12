package com.aidokay.music.server

import cats.data.Reader
import cats.Monad
import cats.effect.Concurrent
import com.aidokay.music.model.{Director, DirectorVar, Movie}
import com.aidokay.music.service.Service
import org.http4s.circe.*
import org.http4s.headers.*
//import org.http4s.implicits.*
import org.http4s.{
  EntityDecoder,
  Header,
  HttpRoutes,
  ParseFailure,
  QueryParamDecoder,
  ResponseCookie,
  ContentCoding
}
import org.http4s.dsl.Http4sDsl
import org.http4s.dsl.impl.{
  OptionalValidatingQueryParamDecoderMatcher,
  QueryParamDecoderMatcher
}
import org.typelevel.ci.CIString
import io.circe.syntax.*
import io.circe.generic.auto.*
import cats.implicits.*

import java.time.Year
import scala.util.Try

object Routes {

  object DirectorQueryParameterMatcher
      extends QueryParamDecoderMatcher[String]("director")

  object YearQueryParameterMatcher
      extends OptionalValidatingQueryParamDecoderMatcher[Int]("year")

  def directorRoutes[F[_]: Concurrent]
      : Reader[Service[Option, Director], HttpRoutes[F]] =
    val dsl = Http4sDsl[F]

    import dsl.*

    given directorDecoder: EntityDecoder[F, Director] = jsonOf[F, Director]

    Reader((dService: Service[Option, Director]) =>
      HttpRoutes.of[F] {
        case GET -> Root / "directors" / DirectorVar(director) => {
          val result = dService.findData(director.toString)
          result match {
            case Some(d) =>
              Ok(d.asJson, Header.Raw(CIString("My-Custom-Header"), "value"))
            case _ => NotFound(s"No director called $director found")
          }
        }
        case req @ POST -> Root / "directors" =>
          for {
            director <- req.as[Director]
            _ = dService.saveData(director)
            res <- Ok
              .headers(`Content-Encoding`(ContentCoding.gzip))
              .map(_.addCookie(ResponseCookie("My-Cookie", "value")))
          } yield res
      }
    )

  given yqp: QueryParamDecoder[Year] = QueryParamDecoder[Int].emap { y =>
    Try(Year.of(y)).toEither.leftMap { tr =>
      ParseFailure(tr.getMessage, tr.getMessage)
    }
  }

  def movieRoutes[F[_]: Monad]: Reader[Service[List, Movie], HttpRoutes[F]] =
    val dsl = Http4sDsl[F]
    import dsl.*

    Reader((movieService: Service[List, Movie]) => {
      HttpRoutes.of[F] {
        case GET -> Root / "movies" :? DirectorQueryParameterMatcher(
              director
            ) +& YearQueryParameterMatcher(year) =>
          val moviesByDirector: List[Movie] = movieService.findData(director)
          year match
            case Some(y) =>
              y.fold(
                _ => BadRequest("The given year is not valid"),
                { yr =>
                  val moviesByDirAndYear = moviesByDirector.filter(_.year == yr)
                  Ok(moviesByDirAndYear.asJson)
                }
              )
            case None => Ok(moviesByDirector.asJson)
        case GET -> Root / "movies" / UUIDVar(movieId) / "actors" =>
          movieService.findDataById(movieId).map(_.actors) match {
            case Some(actors) => Ok(actors.asJson)
            case _            => NotFound(s"No movie with id $movieId found")
          }
      }
    })
}
