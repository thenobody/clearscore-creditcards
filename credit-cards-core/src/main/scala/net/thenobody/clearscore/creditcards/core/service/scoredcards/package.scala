package net.thenobody.clearscore.creditcards.core
package service

import java.net.URI

import net.thenobody.clearscore.creditcards.core.model.scoredcards.{CreditCard => SCCreditCard, _}
import net.thenobody.clearscore.creditcards.core.model.scoredcards.ScoredCardsRequestEnums.{EmploymentStatus => SCEmploymentStatus}
import net.thenobody.clearscore.creditcards.core.model._
import spray.json._

package object scoredcards {

  type ScoredCardsResponse = Seq[SCCreditCard]

  val ProviderName = "ScoredCards"

  implicit class RequestOps(self: Request) {
    def toScoredCardsRequest: ScoredCardsRequest = ScoredCardsRequest(
      firstName = self.firstName,
      lastName = self.lastName,
      dateOfBirth = self.dateOfBirth.toString(Request.DateOfBirthFormatter),
      score = self.score,
      employmentStatus = self.employmentStatus.toScoredCards,
      salary = Some(self.salary)
    )
  }

  implicit class EmploymentStatusOps(self: EmploymentStatus) {
    def toScoredCards: SCEmploymentStatus = self match {
      case EmploymentStatus.FullTime => SCEmploymentStatus.FULLTIME
      case EmploymentStatus.PartTime => SCEmploymentStatus.PARTTIME
      case EmploymentStatus.Retired => SCEmploymentStatus.RETIRED
      case EmploymentStatus.Student => SCEmploymentStatus.STUDENT
      case EmploymentStatus.Unemployed => SCEmploymentStatus.UNEMPLOYED
    }
  }

  implicit class ScoredCardsResponseOps(self: ScoredCardsResponse) {
    def toResponse: Response = self.map {
      case card @ SCCreditCard(cardName, applyUrl, apr, approvalRating, attributes, introductoryOffers) =>
        CreditCard(ProviderName, cardName, new URI(applyUrl), apr, attributes ++ introductoryOffers, card.normalisedScore)
    }
  }

  implicit class CreditCardOps(self: SCCreditCard) {
    def normalisedScore: Double = (self.approvalRating / 1.0) * math.pow(1/self.annualPercentageRate, 2) * 100
  }

  object JsonProtocol extends DefaultJsonProtocol {
    implicit val employmentStatusFormat: RootJsonFormat[SCEmploymentStatus] = new RootJsonFormat[SCEmploymentStatus] {
      def write(obj: SCEmploymentStatus): JsValue = JsString(obj.toString)

      override def read(json: JsValue): SCEmploymentStatus = json match {
        case JsString(status) => SCEmploymentStatus.withName(status)
      }
    }

    implicit val scoredCardsRequestFormat: RootJsonFormat[ScoredCardsRequest] =
      jsonFormat(ScoredCardsRequest.apply, "first-name", "last-name", "date-of-birth", "score", "employment-status", "salary")
    implicit val cardFormat: RootJsonFormat[SCCreditCard] =
      jsonFormat(SCCreditCard.apply, "card", "apply-url", "annual-percentage-rate", "approval-rating", "attributes", "introductory-offers")
  }

}
