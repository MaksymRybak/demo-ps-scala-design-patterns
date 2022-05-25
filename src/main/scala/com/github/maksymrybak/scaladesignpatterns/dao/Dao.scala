package com.github.maksymrybak.scaladesignpatterns.dao

import com.github.maksymrybak.scaladesignpatterns.model.Model.Entity

import java.util.UUID
import scala.concurrent.Future

trait Dao[T <: Entity] {
  // "T <: Entity" is type bounce for T, it means that concrete type should be subtype of Entity

  def insert(entity: T): Future[T]

  def byId(id: UUID): Future[Option[T]]

  def all: Future[Seq[T]]

  def update(id: UUID, entity: T): Future[T]

  def remove(id: UUID): Future[Boolean]
}
