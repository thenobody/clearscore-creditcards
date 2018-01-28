package net.thenobody.clearscore.creditcards.service

import cats.syntax.either._
import net.thenobody.clearscore.creditcards.core.model.{
  EmploymentStatus,
  Request
}
import com.github.nscala_time.time.Imports._

import scala.util.Try

package object api {

  type Result[T] = Either[Error, T]

  implicit class ApiRequestOps(request: ApiRequest) {
    def toRequest: Either[Error, Request] =
      for {
        dateOfBirth <- validDateOfBirth
        employmentStatus <- validEmploymentStatus
        score <- validScore
        salary <- validSalary
      } yield
        Request(firstName = request.firstName,
                lastName = request.lastName,
                dateOfBirth = dateOfBirth,
                score = score,
                employmentStatus = employmentStatus,
                salary = salary)

    def validDateOfBirth: Result[DateTime] =
      Try {
        Request.DateOfBirthFormatter.parseDateTime(request.dob)
      }.toEither.leftMap(_ => Errors.InvalidDateOfBirth)

    def validEmploymentStatus: Result[EmploymentStatus] =
      Either.fromOption(EmploymentStatus.withNameOption(request.employmentStatus), Errors.InvalidEmploymentStatus)

    def validScore: Result[Int] = request.score match {
      case s if s >= 0 && s <= 700 => Right(s)
      case _                       => Left(Errors.InvalidCreditScore)
    }

    def validSalary: Result[Int] = request.salary match {
      case s if s >= 0 => Right(s)
      case _           => Left(Errors.InvalidSalary)
    }
  }

  sealed trait Error
  object Errors {
    case object InvalidEmploymentStatus extends Error
    case object InvalidDateOfBirth extends Error
    case object InvalidCreditScore extends Error
    case object InvalidSalary extends Error
  }

}
