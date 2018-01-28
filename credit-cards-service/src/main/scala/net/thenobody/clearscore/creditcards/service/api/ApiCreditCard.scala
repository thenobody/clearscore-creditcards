package net.thenobody.clearscore.creditcards.service.api

case class ApiCreditCard(
  provider: String,
  name: String,
  applyUrl: String,
  apr: BigDecimal,
  features: Seq[String],
  cardScore: BigDecimal
)