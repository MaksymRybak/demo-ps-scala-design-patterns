package com.github.maksymrybak.scaladesignpatterns

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.Materializer
import com.github.maksymrybak.scaladesignpatterns.routes.ServiceRoutes
import com.typesafe.config.{Config, ConfigFactory}

import scala.util.{Failure, Success}

class Backend private(config: Config, serviceRoutes: Route)
                     (implicit system: ActorSystem, matzr: Materializer) {

  implicit def executor = system.dispatcher
  private val logger = Logging(system, getClass)

  private val HOST = config.getString("http.interface")
  private val PORT = config.getInt("http.port")

  Http()
    .newServerAt(HOST, PORT)
    .bind(serviceRoutes)
    .onComplete {
      case Success(bound) =>
        logger.info(s"Server started: ${bound.localAddress.getHostString}")
      case Failure(e) =>
        logger.error(s"Server couldn't start: ${e.getMessage}")
        system.terminate()
    }
}

object Backend extends App {
  implicit val actorSystem: ActorSystem = ActorSystem()
  implicit val matzr: Materializer = Materializer(actorSystem)

  final val config: Config = ConfigFactory.load("application.conf")

  new Backend(config, ServiceRoutes.route)
}
