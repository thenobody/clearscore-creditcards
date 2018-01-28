package net.thenobody.clearscore.creditcards.service

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.{Directives, MalformedRequestContentRejection, RejectionHandler, ValidationRejection}
import spray.json.DefaultJsonProtocol

object Handlers extends SprayJsonSupport with DefaultJsonProtocol {

  import Directives._

  val rejectionHandler: RejectionHandler = RejectionHandler
    .newBuilder()
    .handle {
      case ValidationRejection(cause, _) =>
        complete(StatusCodes.BadRequest -> Map("error" -> cause))
      case MalformedRequestContentRejection(cause, _) =>
        complete(StatusCodes.BadRequest -> Map("error" -> cause))
    }
    .handleNotFound { complete(StatusCodes.NotFound) }
    .result

}
