package net.thenobody.clearscore.creditcards.core.service.scoredcards

import net.thenobody.clearscore.creditcards.ModelGenerators._
import net.thenobody.clearscore.creditcards.core.model.{EmploymentStatus, Request}
import net.thenobody.clearscore.creditcards.core.model.EmploymentStatus._
import net.thenobody.clearscore.creditcards.core.model.scoredcards.ScoredCardsRequest
import org.scalatest.prop.PropertyChecks
import org.scalatest.{FlatSpec, Matchers}
import net.thenobody.clearscore.creditcards.core.model.scoredcards.ScoredCardsRequestEnums.{EmploymentStatus => SCEmploymentStatus}

class RequestOpsTest extends FlatSpec with Matchers with PropertyChecks {

  behavior of classOf[RequestOpsTest].getSimpleName

  it should "convert a Request into a ScoredCardsRequest" in {
    forAll(aFirstName, aLastName, aDate, aCreditScore, anEmploymentStatus, aSalary) {
      (firstName, lastName, date, score, employmentStatus, salary) =>
        val dateOfBirth = date.toString("yyyy/MM/dd")
        val request = Request(firstName, lastName, date, score, employmentStatus, salary)
        val expResult = ScoredCardsRequest(firstName, lastName, dateOfBirth, score, scEmploymentStatus(employmentStatus), Some(salary))

        val result = new RequestOps(request).toScoredCardsRequest

        result shouldEqual expResult
    }
  }

  def scEmploymentStatus(status: EmploymentStatus): SCEmploymentStatus = status match {
    case FullTime => SCEmploymentStatus.FULLTIME
    case PartTime => SCEmploymentStatus.PARTTIME
    case Student => SCEmploymentStatus.STUDENT
    case Unemployed => SCEmploymentStatus.UNEMPLOYED
    case Retired => SCEmploymentStatus.RETIRED
  }
}
