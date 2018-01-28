package net.thenobody.clearscore.creditcards.core.service

import net.thenobody.clearscore.creditcards.core.model.{CreditCard, Request}


trait CardsService {

  def getCardsByRequest(request: Request): Seq[CreditCard]

}