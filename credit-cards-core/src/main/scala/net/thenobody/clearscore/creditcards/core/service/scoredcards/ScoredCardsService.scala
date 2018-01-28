package net.thenobody.clearscore.creditcards.core
package service
package scoredcards

import akka.actor.ActorSystem
import net.thenobody.clearscore.creditcards.core.model.scoredcards._
import net.thenobody.clearscore.creditcards.core.model.{CreditCard, Request}

import scala.concurrent.{ExecutionContext, Future}

class ScoredCardsService(
    client: ScoredCardsRequest => Future[ScoredCardsResponse])(
    implicit ec: ExecutionContext,
    actorSystem: ActorSystem)
    extends CardsService {

  def getCardsByRequest(request: Request): Future[Seq[CreditCard]] =
    client(request.toScoredCardsRequest).map(_.toResponse)
}
