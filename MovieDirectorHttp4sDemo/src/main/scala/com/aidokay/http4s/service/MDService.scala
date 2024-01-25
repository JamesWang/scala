package com.aidokay.http4s.service

import cats.data.Reader
import com.aidokay.http4s.repo.Repos.Repository

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
  this: MovieService[F, T] =>
  val repository: Repository[F, String, T]

  override def saveData(t: T): Option[T] = save(t).run(repository)

  override def findData(key: String): F[T] =
    find(key).run.apply(repository)

  override def findDataById(id: UUID): Option[T] =
    findById(id).run(repository)
