package net.thenobody.clearscore.creditcards
package core
package service
package cscards

import akka.actor.ActorSystem
import net.thenobody.clearscore.creditcards.core.model.cscards.CardSearchRequest
import net.thenobody.clearscore.creditcards.core.model.{CreditCard, Request}

import scala.concurrent.{ExecutionContext, Future}

class CSCardsService(client: CardSearchRequest => Future[CardSearchResponse])(
    implicit ec: ExecutionContext,
    actorSystem: ActorSystem)
    extends CardsService {

  def getCardsByRequest(request: Request): Future[Seq[CreditCard]] =
    client(request.toScoredCardsRequest).map(_.toResponse)
}
