package net.thenobody.clearscore.creditcards.core.service.cscards

import java.net.URI

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import com.typesafe.config.Config
import spray.json._
import JsonProtocol._
import net.thenobody.clearscore.creditcards.core.model.cscards._

import scala.concurrent.{ExecutionContext, Future}

trait CSCardsClient {
  def execute(request: CardSearchRequest): Future[CardSearchResponse]
}

object CSCardsClient {
  sealed trait Error extends Exception

  object Errors {
    case class Failure(status: Int, reason: String) extends Error
  }
}

class CSCardsClientImpl(baseEndpoint: URI)(implicit ec: ExecutionContext, actorSystem: ActorSystem) extends CSCardsClient {
  import CSCardsClientImpl._
  import CSCardsClient.Errors

  def execute(request: CardSearchRequest): Future[CardSearchResponse] = {
    val httpRequest = HttpRequest()
      .withMethod(HttpMethods.POST)
      .withUri(Uri(baseEndpoint.toString + CardsEndpoint))
      .withEntity(HttpEntity(ContentTypes.`application/json`, request.toJson.toString))

    Http().singleRequest(httpRequest).flatMap {
      case HttpResponse(StatusCodes.OK, _, HttpEntity.Strict(_, data), _) =>
        Future.successful(data.utf8String.parseJson.convertTo[Seq[Card]])
      case HttpResponse(status, _, HttpEntity.Strict(_, data), _) =>
        Future.failed(Errors.Failure(status.intValue, data.utf8String))
    }
  }
}

object CSCardsClientImpl {
  val CardsEndpoint = "/v1/cards"
  val CardsContentType = "application/json"

  def apply(config: Config)(implicit ec: ExecutionContext, actorSystem: ActorSystem): CSCardsClientImpl =
    new CSCardsClientImpl(
      new URI(config.getString("credit-cards.cs-cards.baseEndpoint"))
    )
}
