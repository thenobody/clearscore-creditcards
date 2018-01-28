package net.thenobody.clearscore.creditcards
package service
package api

import net.thenobody.clearscore.creditcards.ModelGenerators._
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
    forAll(aFirstName, aLastName, anEmploymentStatusString, aCreditScore, aSalary) {
      (firstName, lastName, employmentStatus, score, salary) =>
        val apiRequest = ApiRequest(firstName,
                                    lastName,
                                    "INVALID",
                                    score,
                                    employmentStatus,
                                    salary)

        val result = new ApiRequestOps(apiRequest).toRequest

        result.left.value shouldEqual Errors.InvalidDateOfBirth
    }
  }

  it should "report on invalid employment status" in {
    forAll(aFirstName, aLastName, aDate, aCreditScore, aSalary) { (firstName, lastName, date, score, salary) =>
      val apiRequest = ApiRequest(firstName,
                                  lastName,
                                  date.toString("yyyy/MM/dd"),
                                  score,
                                  "INVALID",
                                  salary)

      val result = new ApiRequestOps(apiRequest).toRequest

      result.left.value shouldEqual Errors.InvalidEmploymentStatus("INVALID")
    }
  }

  it should "report on invalid credit score" in {
    forAll(aFirstName, aLastName, aDate, anEmploymentStatusString, aSalary) {
      (firstName, lastName, date, employmentStatus, salary) =>
        val apiRequest1 = ApiRequest(firstName,
                                     lastName,
                                     date.toString("yyyy/MM/dd"),
                                     -1,
                                     employmentStatus,
                                     salary)
        val apiRequest2 = ApiRequest(firstName,
                                     lastName,
                                     date.toString("yyyy/MM/dd"),
                                     701,
                                     employmentStatus,
                                     salary)

        val result1 = new ApiRequestOps(apiRequest1).toRequest
        val result2 = new ApiRequestOps(apiRequest2).toRequest

        result1.left.value shouldEqual Errors.InvalidCreditScore
        result2.left.value shouldEqual Errors.InvalidCreditScore
    }
  }

  it should "report on invalid salary" in {
    forAll(aFirstName, aLastName, aDate, anEmploymentStatusString, aCreditScore) {
      (firstName, lastName, date, employmentStatus, score) =>
        val apiRequest = ApiRequest(firstName,
                                    lastName,
                                    date.toString("yyyy/MM/dd"),
                                    score,
                                    employmentStatus,
                                    -1)

        val result = new ApiRequestOps(apiRequest).toRequest

        result.left.value shouldEqual Errors.InvalidSalary
    }
  }
}
