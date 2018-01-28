package net.thenobody.clearscore.creditcards.core.model.cscards

case class CardSearchRequest (
  /* Users full name */
  fullName: String,
  /* Users date of birth, formatted as yyyy/MM/dd */
  dateOfBirth: String,
  /* Credit score between 0 and 700 */
  creditScore: Int
)