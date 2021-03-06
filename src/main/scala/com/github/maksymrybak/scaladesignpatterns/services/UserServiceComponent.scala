package com.github.maksymrybak.scaladesignpatterns.services

import com.github.maksymrybak.scaladesignpatterns.dao.UserDaoComponent
import com.github.maksymrybak.scaladesignpatterns.model.Model.User

import java.util.UUID
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

trait UserServiceComponent {
  /** Self type annotation */
  self: UserDaoComponent =>

  implicit val userClient: ServiceClient[User]

  class UserServiceImpl extends ServiceClient[User] {

    override def create(entity: User): Future[ServiceResponse[User]] =
      userDao
        .insert(entity)
        .map(Right(_))
        .recover {
          case e: Exception => Left(ErrorResponse(e.getMessage, 0))
        }


    override def read(id: UUID): Future[ServiceResponse[User]] =
      userDao
        .byId(id)
        .map {
          case None => Left(ErrorResponse(s"Couldn't find User with Id: $id", 0))
          case Some(user) => Right(user)
        }
        .recover {
          case e: Exception => Left(ErrorResponse(e.getMessage, 0))
        }

    override def update(id: UUID, entity: User): Future[ServiceResponse[User]] =
      userDao
        .update(id, entity)
        .map(Right(_))
        .recover {
          case e: Exception => Left(ErrorResponse(e.getMessage, 0))
        }

    override def delete(id: UUID): Future[ServiceResponse[Boolean]] =
      userDao
        .remove(id)
        .map {
          case true => Right(true)
          case _ => Left(ErrorResponse("Could not delete User with given ID", 0))
        }
        .recover{
          case e: Exception => Left(ErrorResponse(e.getMessage, 0))
        }
  }
}
