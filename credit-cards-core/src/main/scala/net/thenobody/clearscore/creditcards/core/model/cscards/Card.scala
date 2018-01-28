package net.thenobody.clearscore.creditcards.core.model.cscards

case class Card (
  /* Name of the credit card product */
  cardName: String,
  /* URL the user can follow to apply for the credit card */
  url: String,
  /* Annual percentage rate for the card */
  apr: Double,
  /* How likely the user is to be approved ranging from 0.0 to 10.0 */
  eligibility: Double,
  /* List of features and benefits the card offers */
  features: Option[Seq[String]]
)