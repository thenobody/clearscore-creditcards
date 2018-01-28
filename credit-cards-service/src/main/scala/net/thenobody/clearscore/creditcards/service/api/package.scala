package net.thenobody.clearscore.creditcards.service

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.github.nscala_time.time.Imports._
import net.thenobody.clearscore.creditcards.core.model.{CreditCard, EmploymentStatus, Request}
import spray.json._

import scala.util.Try

package object api {

  type Result[T] = Either[Error, T]

  type ApiResponse = Seq[ApiCreditCard]

  implicit class ApiRequestOps(request: ApiRequest) {
    import cats.syntax.either._

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
      Either.fromOption(EmploymentStatus.withNameOption(request.employmentStatus), Errors.InvalidEmploymentStatus(request.employmentStatus))

    def validScore: Result[Int] = request.score match {
      case s if s >= 0 && s <= 700 => Right(s)
      case _                       => Left(Errors.InvalidCreditScore)
    }

    def validSalary: Result[Int] = request.salary match {
      case s if s >= 0 => Right(s)
      case _           => Left(Errors.InvalidSalary)
    }
  }

  implicit class CreditCardOps(self: CreditCard) {
    def toApiCreditCard: ApiCreditCard = ApiCreditCard(
      provider = self.provider,
      name = self.name,
      applyUrl = self.applyUrl.toString,
      apr = BigDecimal(self.apr).setScale(2, BigDecimal.RoundingMode.HALF_UP),
      features = self.features,
      cardScore = BigDecimal(self.cardScore).setScale(3, BigDecimal.RoundingMode.HALF_UP)
    )
  }

  sealed trait Error
  object Errors {
    case class InvalidEmploymentStatus(invalid: String) extends Error
    case object InvalidDateOfBirth extends Error
    case object InvalidCreditScore extends Error
    case object InvalidSalary extends Error
  }

  object JsonProtocol extends SprayJsonSupport with DefaultJsonProtocol {
    implicit val apiRequestFormat: RootJsonFormat[ApiRequest] =
      jsonFormat(ApiRequest.apply, "firstname", "lastname", "dob", "credit-score", "employment-status", "salary")
    implicit val apiCreditCardFormat: RootJsonFormat[ApiCreditCard] =
      jsonFormat(ApiCreditCard.apply, "provider", "name", "apply-url", "apr", "features", "card-score")
  }

}
