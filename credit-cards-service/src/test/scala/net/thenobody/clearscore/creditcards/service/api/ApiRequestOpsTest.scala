package net.thenobody.clearscore.creditcards
package service
package api

import ExtraGenerators._
import net.thenobody.clearscore.creditcards.core.model.{EmploymentStatus, Request}
import org.scalatest.prop.PropertyChecks
import org.scalatest.{EitherValues, FlatSpec, Matchers}

class ApiRequestOpsTest
    extends FlatSpec
    with Matchers
    with PropertyChecks
    with EitherValues {

  behavior of classOf[ApiRequestOps].getSimpleName

  it should "convert an ApiRequest into a validated Request instance" in {
    forAll(aFirstName, aLastName, aDate, anEmploymentStatusString, aCreditScore, aSalary) {
      (firstName, lastName, date, employmentStatus, score, salary) =>
        val apiRequest = ApiRequest(firstName,
                                    lastName,
                                    date.toString("yyyy/MM/dd"),
                                    score,
                                    employmentStatus,
                                    salary)

        val result = new ApiRequestOps(apiRequest).toRequest
        val expResult =
          Request(firstName, lastName, date, score, EmploymentStatus.withName(employmentStatus), salary)

      result.right.value shouldEqual expResult
    }
  }

  it should "report on invalid date of birth" in {
    forAll(anApiRequest) { apiRequest =>
        val result = new ApiRequestOps(apiRequest.copy(dob = "INVALID")).toRequest

        result.left.value shouldEqual Errors.InvalidDateOfBirth
    }
  }

  it should "report on invalid employment status" in {
    forAll(anApiRequest) { apiRequest =>
      val result = new ApiRequestOps(apiRequest.copy(employmentStatus = "INVALID")).toRequest

      result.left.value shouldEqual Errors.InvalidEmploymentStatus("INVALID")
    }
  }

  it should "report on invalid credit score" in {
    forAll(anApiRequest) { apiRequest =>
        val apiRequest1 = apiRequest.copy(score = -1)
        val apiRequest2 = apiRequest.copy(score = 701)
        val result1 = new ApiRequestOps(apiRequest1).toRequest
        val result2 = new ApiRequestOps(apiRequest2).toRequest

        result1.left.value shouldEqual Errors.InvalidCreditScore
        result2.left.value shouldEqual Errors.InvalidCreditScore
    }
  }

  it should "report on invalid salary" in {
    forAll(anApiRequest) { apiRequest =>
        val result = new ApiRequestOps(apiRequest.copy(salary = -1)).toRequest

        result.left.value shouldEqual Errors.InvalidSalary
    }
  }
}
