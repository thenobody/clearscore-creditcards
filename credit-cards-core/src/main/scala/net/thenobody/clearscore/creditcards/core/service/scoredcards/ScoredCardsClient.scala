package net.thenobody.clearscore.creditcards.core.service.scoredcards

import java.net.URI

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import com.typesafe.config.Config
import net.thenobody.clearscore.creditcards.core.model.scoredcards._
import spray.json._
import JsonProtocol._

import scala.concurrent.{ExecutionContext, Future}

trait ScoredCardsClient {
  def execute(request: ScoredCardsRequest): Future[ScoredCardsResponse]
}

object ScoredCardsClient {
  sealed trait Error extends Exception

  object Errors {
    case class Failure(status: Int, reason: String) extends Error
  }
}

class ScoredCardsClientImpl(baseEndpoint: URI)(implicit ec: ExecutionContext, actorSystem: ActorSystem) extends ScoredCardsClient {
  import ScoredCardsClientImpl._
  import ScoredCardsClient._

  def execute(request: ScoredCardsRequest): Future[ScoredCardsResponse] = {
    val httpRequest = HttpRequest()
      .withMethod(HttpMethods.POST)
      .withUri(Uri(baseEndpoint.toString + CreditCardsEndpoint))
      .withEntity(HttpEntity(ContentTypes.`application/json`, request.toJson.toString))

    Http().singleRequest(httpRequest).flatMap {
      case HttpResponse(StatusCodes.OK, _, HttpEntity.Strict(_, data), _) =>
        Future.successful(data.utf8String.parseJson.convertTo[Seq[CreditCard]])
      case HttpResponse(status, _, HttpEntity.Strict(_, data), _) =>
        Future.failed(Errors.Failure(status.intValue, data.utf8String))
    }
  }
}

object ScoredCardsClientImpl {
  val CreditCardsEndpoint = "/v2/creditcards"
  val CreditCardsContentType = "application/json"

  def apply(config: Config)(implicit ec: ExecutionContext, actorSystem: ActorSystem): ScoredCardsClientImpl =
    new ScoredCardsClientImpl(
      new URI(config.getString("credit-cards.scored-cards.baseEndpoint"))
    )
}
