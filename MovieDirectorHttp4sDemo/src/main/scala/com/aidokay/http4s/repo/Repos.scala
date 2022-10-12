package com.aidokay.music.repo

import java.util.UUID
import scala.collection.mutable
import com.aidokay.music.model.{Director, Movie}
object Repos {
  trait Repository[F[_], K, T] {
    def find(key: K): F[T]

    def save(t: T): Option[T]

    def findById(id: UUID): Option[T]
  }

  given directorRepo: Repository[Option, String, Director] with
    private val directors: mutable.Map[String, Director] =
      mutable.Map("AZack Snyder" -> Director("Zack", "Snyder"))

    override def save(director: Director): Option[Director] =
      directors.put(director.toString, director)

    override def find(director: String): Option[Director] =
      directors.get(director)

    override def findById(id: UUID): Option[Director] = None

  given movieRepo: Repository[List, String, Movie] with
    private val aMovie: Movie = Movie(
      "uuid-abcdefgh-3232323-2222",
      "Zack Snyder's Justice League",
      2021,
      List(
        "Henry Cavill",
        "Gal Godot",
        "Ezra Miller",
        "Ben Affleck",
        "Ray Fist"
      ),
      "Zack Snyder"
    )

    private val movies: Map[String, Movie] = Map(aMovie.id -> aMovie)

    override def find(key: String): List[Movie] =
      movies.values.filter(_.director == key).toList

    override def findById(id: UUID): Option[Movie] = movies.get(id.toString)

    override def save(t: Movie): Option[Movie] = ???
}
