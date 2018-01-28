package net.thenobody.clearscore.creditcards.core.model.scoredcards

case class CreditCard (
  /* Name of the credit card */
  card: String,
  /* URL the user can follow to apply for the credit card */
  applyUrl: String,
  /* Annual percentage rate for the card */
  annualPercentageRate: Double,
  /* The likelihood of the user being approved, from 0.0 to 1.0 */
  approvalRating: Double,
  /* List of features of the credit card */
  attributes: Seq[String],
  /* List of introductory offers for the credit card */
  introductoryOffers: Seq[String]
)