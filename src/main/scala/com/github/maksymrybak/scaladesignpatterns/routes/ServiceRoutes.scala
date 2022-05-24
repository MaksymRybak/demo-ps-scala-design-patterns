package com.github.maksymrybak.scaladesignpatterns.routes

import akka.http.scaladsl.server.Route

object ServiceRoutes extends HelloRoute {

  val route: Route = hello

}
