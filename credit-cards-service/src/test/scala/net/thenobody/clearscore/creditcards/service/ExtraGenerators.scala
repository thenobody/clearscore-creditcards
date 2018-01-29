package net.thenobody.clearscore.creditcards.service

import net.thenobody.clearscore.creditcards.ModelGenerators
import net.thenobody.clearscore.creditcards.service.api.ApiRequest
import org.scalacheck.Gen

object ExtraGenerators extends ModelGenerators {

  def anApiRequest: Gen[ApiRequest] = for {
    firstName <- aFirstName
    lastName <- aLastName
    dateOfBirth <- aDate.map(_.toString("yyyy/MM/dd"))
    creditScore <- aCreditScore
    employmentStatus <- anEmploymentStatus
    salary <- aSalary
  } yield ApiRequest(firstName, lastName, dateOfBirth, creditScore, employmentStatus.entryName, salary)

}
