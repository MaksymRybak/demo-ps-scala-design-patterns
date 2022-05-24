package com.github.maksymrybak.scaladesignpatterns.services

import com.github.maksymrybak.scaladesignpatterns.model.Model.User

import java.util.UUID
import scala.concurrent.ExecutionContext.Implicits.global   // THIS ONE WE HAVE TO ADD, why??
import scala.concurrent.Future

class UserServiceClient extends ServiceClient[User] {

  override def create(entity: User): Future[ServiceResponse[User]] =
    Future {
      println(s"ServiceClient: Creating a User with ID: ${entity.id}")

      Right(entity)
    }

  override def read(id: UUID): Future[ServiceResponse[User]] =
    Future {
      println(s"ServiceClient: Reading a User with ID: ${id}")

      Left(ErrorResponse(s"No User with ID: ${id}", 0))
    }

  override def update(id: UUID, entity: User): Future[ServiceResponse[User]] =
    Future {
      println(s"ServiceClient: Updating a User with ID: ${id}")

      Left(ErrorResponse(s"No User with ID: ${id}", 0))
    }

  override def delete(id: UUID): Future[ServiceResponse[Boolean]] =
    Future {
      println(s"ServiceClient: Deleting a User with ID: ${id}")

      Left(ErrorResponse(s"No User with ID: ${id}", 0))
    }
}
