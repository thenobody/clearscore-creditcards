package net.thenobody.clearscore.creditcards.core.model.scoredcards

case class ScoredCardsRequest (
  /* Users first name */
  firstName: String,
  /* Users last name */
  lastName: String,
  /* Users date of birth, formatted as yyyy/MM/dd */
  dateOfBirth: String,
  /* Credit score between 0 and 700 */
  score: Int,
  /* Users employment status */
  employmentStatus: ScoredCardsRequestEnums.EmploymentStatus,
  salary: Option[Int]
)

object ScoredCardsRequestEnums {

  type EmploymentStatus = EmploymentStatus.Value
  object EmploymentStatus extends Enumeration {
    val FULLTIME = Value("FULL_TIME")
    val PARTTIME = Value("PART_TIME")
    val STUDENT = Value("STUDENT")
    val UNEMPLOYED = Value("UNEMPLOYED")
    val RETIRED = Value("RETIRED")
  }

}