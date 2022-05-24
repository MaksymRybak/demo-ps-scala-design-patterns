package com.github.maksymrybak.scaladesignpatterns.routes

import akka.http.scaladsl.server.Directives.{as, complete, entity, pathEndOrSingleSlash, pathPrefix, post}
import akka.http.scaladsl.server.Route
import com.github.maksymrybak.scaladesignpatterns.model.Model._
import com.github.maksymrybak.scaladesignpatterns.services._

import scala.concurrent.Future

trait UserRoute {

  private val USER_API = "user"

  val user: Route =
    pathPrefix(USER_API) {

      pathEndOrSingleSlash {

        /** POST request to CREATE User */
        (post & entity(as[User])) { user =>

          val createUserResult: Future[ServiceResponse[User]] = create(user)
          respondWith(createUserResult)

        }

      }

    }
}
