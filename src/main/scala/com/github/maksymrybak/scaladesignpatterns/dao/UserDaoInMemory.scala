package com.github.maksymrybak.scaladesignpatterns.dao

import com.github.maksymrybak.scaladesignpatterns.model.Model.User
import com.github.maksymrybak.scaladesignpatterns.services.ErrorResponse

import java.util.UUID
import scala.concurrent.Future
import scala.util.{Failure, Success, Try}
import scala.concurrent.ExecutionContext.Implicits.global

class UserDaoInMemory extends Dao[User] {
  private var users = Map[UUID, User]()

  override def insert(user: User): Future[User] = Future {
    Try(users.+((user.id, user)))
      .map(updated => users = updated) match {
        case Failure(exception) => throw ErrorResponse(exception.getMessage, 0)
        case Success(_) => user
    }
  }

  override def byId(id: UUID): Future[Option[User]] = Future {
    users.get(id)
  }

  override def all: Future[Seq[User]] = Future {
    users.values.toList
  }

  override def update(id: UUID, t: User): Future[User] = {
    users.get(id) match {
      case None => Future.failed(ErrorResponse(s"Couldn't find order with id: $id", 0))
      case Some(_) =>
        Try(users.-(id)) match {
          case Failure(e) => Future.failed(ErrorResponse(e.getMessage, 0))
          case Success(_) => insert(t)
        }
    }
  }

  override def remove(id: UUID): Future[Boolean] = Future {
    users.get(id) match {
      case None => throw ErrorResponse(s"Couldn't find order with id: $id", 0)
      case Some(_) =>
        Try(users.-(id)) match {
          case Failure(e) => throw ErrorResponse(e.getMessage, 0)
          case Success(_) => true
        }
    }
  }
}
