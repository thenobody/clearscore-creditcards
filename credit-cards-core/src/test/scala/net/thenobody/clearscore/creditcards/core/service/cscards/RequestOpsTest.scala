package net.thenobody.clearscore.creditcards.core.service.cscards

import net.thenobody.clearscore.creditcards.ModelGenerators._
import net.thenobody.clearscore.creditcards.core.model.Request
import net.thenobody.clearscore.creditcards.core.model.cscards.CardSearchRequest
import org.scalatest.prop.PropertyChecks
import org.scalatest.{FlatSpec, Matchers}

class RequestOpsTest extends FlatSpec with Matchers with PropertyChecks {

  behavior of classOf[RequestOps].getSimpleName

  it should "convert a Request into a CardSearchRequest" in {
    forAll(aFirstName, aLastName, aDate, aCreditScore, anEmploymentStatus, aSalary) {
      (firstName, lastName, date, score, employmentStatus, salary) =>
        val dateOfBirth = date.toString("yyyy/MM/dd")
        val request = Request(firstName, lastName, date, score, employmentStatus, salary)

        val expResult = CardSearchRequest(s"$firstName $lastName", dateOfBirth, score)

        val result = new RequestOps(request).toScoredCardsRequest

        result shouldEqual expResult
    }
  }

}
