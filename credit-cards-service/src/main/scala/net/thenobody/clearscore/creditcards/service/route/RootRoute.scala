package net.thenobody.clearscore.creditcards.service.route

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Route, ValidationRejection}
import akka.stream.ActorMaterializer
import cats.implicits._
import net.thenobody.clearscore.creditcards.core.service.CardsService
import net.thenobody.clearscore.creditcards.service.api._
import net.thenobody.clearscore.creditcards.service.api.JsonProtocol._
import net.thenobody.clearscore.creditcards.service.{Handlers, LoggingUtils}

import scala.concurrent.{ExecutionContext, Future}

class RootRoute(
  cardServices: Seq[CardsService]
)(implicit ec: ExecutionContext, actorSystem: ActorSystem, materializer: ActorMaterializer) extends LoggingUtils {

  def build: Route = logRequestResult(Logging.InfoLevel) {
    handleRejections(Handlers.rejectionHandler) {
      creditCardsRoute ~
      pingRoute
    }
  }

  private val creditCardsRoute =
    path("creditcards") {
      post {
        entity(as[ApiRequest]) { request =>
          getAllCreditCards(request) match {
            case Right(results) =>
              onSuccess(results) { extraction: Seq[ApiCreditCard] =>
                complete(extraction)
              }
            case Left(reason) =>
              reject(ValidationRejection(reason.toString))
          }
        }
      }
    }

  private val pingRoute =
    path("ping") {
      get {
        complete("pong")
      }
    }

  private def getAllCreditCards(request: ApiRequest): Result[Future[ApiResponse]] =
    request
      .toRequest
      .map { valid =>
        cardServices
          .map(_.getCardsByRequest(valid))
          .toList
          .sequence
          .map(_.flatten.map(_.toApiCreditCard).sortBy(_.cardScore).reverse)
      }
}
