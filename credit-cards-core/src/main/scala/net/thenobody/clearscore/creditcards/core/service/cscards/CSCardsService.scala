package net.thenobody.clearscore.creditcards
package core
package service
package cscards

import net.thenobody.clearscore.client.cscardsswagger.model.CardSearchRequest
import net.thenobody.clearscore.creditcards.core.model.{CreditCard, Request}

class CSCardsService(client: CardSearchRequest => CardSearchResponse) extends CardsService {

  def getCardsByRequest(request: Request): Seq[CreditCard] = {
    ???
  }
}