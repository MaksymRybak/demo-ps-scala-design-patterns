package com.github.maksymrybak.scaladesignpatterns.routes

import akka.http.scaladsl.server.Route
import com.github.maksymrybak.scaladesignpatterns.dao.{Dao, UserDaoComponent, UserDaoInMemory}
import com.github.maksymrybak.scaladesignpatterns.model.Model
import com.github.maksymrybak.scaladesignpatterns.services.{ServiceClient, UserServiceComponent}

/** Example of Cake Pattern, implemented using self type annotation */
object ServiceRoutes
  extends UserRoute
    with UserServiceComponent
    with UserDaoComponent {

  override val userDao: Dao[Model.User] = new UserDaoInMemory
  override implicit val userClient: ServiceClient[Model.User] = new UserServiceImpl

  val route: Route = user

}
