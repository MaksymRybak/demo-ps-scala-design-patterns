package com.github.maksymrybak.scaladesignpatterns.routes

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives.{complete, get, path}

trait HelloRoute {

  private val HELLO_API = "hello"

  val hello: Route =
    path(HELLO_API) {
      get {
        complete(StatusCodes.OK, "Hello from Application")
      }
    }

}
