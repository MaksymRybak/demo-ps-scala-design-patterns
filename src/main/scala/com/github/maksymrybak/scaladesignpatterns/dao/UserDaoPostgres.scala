package com.github.maksymrybak.scaladesignpatterns.dao

import com.github.maksymrybak.scaladesignpatterns.model.Model._
import com.github.maksymrybak.scaladesignpatterns.model.Model.UserRole.UserRole
import com.github.maksymrybak.scaladesignpatterns.services.ErrorResponse

import java.util.UUID
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class UserDaoPostgres extends Dao[User] {
  val driver = slick.jdbc.PostgresProfile
  val db = driver.api.Database.forConfig("conferencedb")

  import driver.api._

  val table = TableQuery[UserTable]

  dropSchema().map(_ => createSchema())

  def dropSchema(): Future[Unit] = db.run {
    table.schema.dropIfExists
  }

  def createSchema(): Future[Unit] = db.run {
    table.schema.create
  }

  override def insert(user: User): Future[User] = db.run {
    table returning table += user
  }

  override def byId(id: UUID): Future[Option[User]] = db.run {
    table.filter(_.id === id).result.headOption
  }

  override def all: Future[Seq[User]] = db.run {
    table.to[Seq].result
  }

  override def update(id: UUID, user: User): Future[User] = {
    db.run {
      table insertOrUpdate user
    }.map(success =>
      if (success == 1) user
      else throw ErrorResponse(s"Couldn't update User with ID ${id}", 0)
    )
  }

  override def remove(id: UUID): Future[Boolean] = db.run {
    table.filter(_.id === id).delete.map(_ > 0)
  }

  implicit def userRoleMapper =
    MappedColumnType.base[UserRole, String](e => e.toString, s => UserRole.withName(s))

  class UserTable(tag: Tag) extends Table[User](tag, "users_t") {
    val id = column[UUID]("id", O.PrimaryKey)

    val pin = column[String]("pin")
    val city = column[String]("city")
    val name = column[String]("name")
    val role = column[UserRole]("role")
    val email = column[String]("email")
    val country = column[String]("country")
    val password = column[String]("password")
    val addressLine = column[String]("address_line")

    def locationMap = (pin, city.?, country.?) <> (Location.tupled, Location.unapply)
    def addressMap = (addressLine.?, locationMap) <> (Address.tupled, Address.unapply)

    def * =
      (id, name, email, password, addressMap, role) <> (User.tupled, User.unapply)
  }
}
