package com.github.maksymrybak.scaladesignpatterns.services

import java.util.UUID
import scala.concurrent.Future

trait ServiceClient[T] {

  def create(entity: T): Future[ServiceResponse[T]]

  def read(id: UUID): Future[ServiceResponse[T]]

  def update(id: UUID, entity: T): Future[ServiceResponse[T]]

  def delete(id: UUID): Future[ServiceResponse[Boolean]]

}

/** Companion object Should be created to make UserServiceClient available through the implicit
 *  Scala compiler can find this implicit instance */
object ServiceClient {

  implicit val userServiceClient = new UserServiceClient

}