package com.github.maksymrybak.scaladesignpatterns

import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse, StatusCode, StatusCodes}
import akka.http.scaladsl.server.Directives.complete
import akka.http.scaladsl.server.StandardRoute
import spray.json.{JsString, JsValue, JsonWriter, enrichAny}

import java.util.UUID
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Try

package object services {

  /** Interface methods */
  def create[T](entity: T)(implicit sc: ServiceClient[T]): Future[ServiceResponse[T]] =
    sc.create(entity)

  def read[T](id: UUID)(implicit sc: ServiceClient[T]): Future[ServiceResponse[T]] =
    sc.read(id)

  def update[T](id: UUID, entity: T)(implicit sc: ServiceClient[T]): Future[ServiceResponse[T]] =
    sc.update(id, entity)

  def delete[T](id: UUID)(implicit sc: ServiceClient[T]): Future[ServiceResponse[Boolean]] =
    sc.delete(id)


  /**
   * Response type alias for typical service response
   */
  type ServiceResponse[T] = Either[ErrorResponse, T]

  case class ErrorResponse(message: String, code: Int) extends Exception

  case class ApiResponse(success: Boolean,
                         code: Option[Int] = None,
                         message: Option[String] = None,
                         data: JsValue = JsString(""))

  /** Helper functions */
  def respondWith[A](response: Future[ServiceResponse[A]])
                    (implicit ee: JsonWriter[ErrorResponse], ar: JsonWriter[ApiResponse], rr: JsonWriter[A]): StandardRoute =
    complete {
      response map {
        case Left(error) =>
          HttpResponse(
            status = toStatusCode(error.code),
            entity =
              HttpEntity(
                ContentTypes.`application/json`,
                ApiResponse(success = false, Some(error.code), Some(error.message)).toJson.toString))

        case Right(value) =>
          HttpResponse(
            status = StatusCodes.Created,
            entity =
              HttpEntity(
                ContentTypes.`application/json`,
                ApiResponse(success = true, data = value.toJson).toJson.toString))
      }
    }

  private def toStatusCode(i: Int): StatusCode =
    Try(StatusCode.int2StatusCode(i))
      .getOrElse(StatusCodes.InternalServerError)
}
