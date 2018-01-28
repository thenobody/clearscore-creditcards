package net.thenobody.clearscore.creditcards.core.service

import net.thenobody.clearscore.creditcards.core.model.{CreditCard, Request}

import scala.concurrent.Future


trait CardsService {

  def getCardsByRequest(request: Request): Future[Seq[CreditCard]]

}