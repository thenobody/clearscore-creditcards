package net.thenobody.clearscore.creditcards.core
package service
package scoredcards

import net.thenobody.clearscore.client.scoredcardsswagger.model.ScoredCardsRequest
import net.thenobody.clearscore.creditcards.core.model.{CreditCard, Request}

class ScoredCardsService(client: ScoredCardsRequest => ScoredCardsResponse) extends CardsService {

  def getCardsByRequest(request: Request): Seq[CreditCard] = {
    ???
  }
}