package com.aidokay.music.service

import cats.data.Reader
import com.aidokay.music.repo.Repos.Repository

import java.util.UUID

trait Service[F[_], T] {
  def saveData(t: T): Option[T]

  def findData(key: String): F[T]

  def findDataById(id: UUID): Option[T]

}

trait MovieService[F[_], T]:
  def save(t: T): Reader[Repository[F, String, T], Option[T]] =
    Reader((repo: Repository[F, String, T]) => repo.save(t))

  def find(key: String): Reader[Repository[F, String, T], F[T]] =
    Reader((repo: Repository[F, String, T]) => repo.find(key))

  def findById(id: UUID): Reader[Repository[F, String, T], Option[T]] =
    Reader((repo: Repository[F, String, T]) => repo.findById(id))

trait MDService[F[_], T] extends Service[F, T]:
  val movieService: MovieService[F, T]
  val repository: Repository[F, String, T]

  override def saveData(t: T): Option[T] = movieService.save(t).run(repository)

  override def findData(key: String): F[T] =
    movieService.find(key).run(repository)

  override def findDataById(id: UUID): Option[T] =
    movieService.findById(id).run(repository)
