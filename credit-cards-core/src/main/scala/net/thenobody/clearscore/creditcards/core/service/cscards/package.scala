package net.thenobody.clearscore.creditcards.core.service

import java.net.URI

import net.thenobody.clearscore.client.cscardsswagger.model.{
  Card,
  CardSearchRequest
}
import net.thenobody.clearscore.creditcards.core.model.{
  CreditCard,
  Request,
  Response
}

package object cscards {

  type CardSearchResponse = Seq[Card]

  val ProviderName = "CSCards"

  implicit class RequestOps(self: Request) {
    def toScoredCardsRequest: CardSearchRequest = CardSearchRequest(
      fullName = s"${self.firstName} ${self.lastName}",
      dateOfBirth = self.dateOfBirth.toString(Request.DateOfBirthFormatter),
      creditScore = self.score
    )
  }

  implicit class CardSearchResponseOps(self: CardSearchResponse) {
    def toResponse: Response = self.map {
      case card @ Card(cardName, url, apr, _, features) =>
        CreditCard(provider = ProviderName,
                   name = cardName,
                   applyUrl = new URI(url),
                   apr = apr,
                   features = features.getOrElse(Seq.empty),
                   cardScore = card.normalisedScore)
    }
  }

  implicit class CardOps(self: Card) {
    def normalisedScore: Double =
      (self.eligibility / 10.0) * math.pow(1 / self.apr, 2) * 100
  }

}
