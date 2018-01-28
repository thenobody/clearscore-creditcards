package net.thenobody.clearscore.creditcards.core.model

import java.net.URI

case class CreditCard(
  provider: String,
  name: String,
  applyUrl: URI,
  apr: Double,
  features: Seq[String],
  cardScore: Double
)
