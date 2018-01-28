package net.thenobody.clearscore.creditcards.core.service.scoredcards

import net.thenobody.clearscore.client.scoredcardsswagger.model.{CreditCard => SCCreditCard}
import org.scalatest.prop.PropertyChecks
import org.scalatest.{FlatSpec, Matchers}
import net.thenobody.clearscore.creditcards.ModelGenerators._
import net.thenobody.clearscore.creditcards.core.model.CreditCard

class ScoredCardsResponseOpsTest extends FlatSpec with Matchers with PropertyChecks {

  behavior of classOf[ScoredCardsResponseOps].getSimpleName

  it should "convert a ScoredCardsResponse into a collection of CreditCard with correct score" in {
    forAll(aCardName, anUri, anAPR, anApprovalRating, aStringList, aStringList) {
      (card, uri, apr, approvalRating, attributes, introductoryOffers) =>
        val score = approvalRating / 1.0 * math.pow(apr / 100.0, -2) / 100
        val scCreditCard = SCCreditCard(card, uri.toString, apr, approvalRating, attributes, introductoryOffers)
        val input = Seq(scCreditCard)

        val creditCard = CreditCard("ScoredCards", card, uri, apr, attributes ++ introductoryOffers, score)
        val expResult = Seq(creditCard)

        val result = new ScoredCardsResponseOps(input).toResponse

        result.size shouldEqual expResult.size
        result.zip(expResult).foreach { case (res, exp) =>
          res.provider shouldEqual exp.provider
          res.name shouldEqual exp.name
          res.applyUrl shouldEqual exp.applyUrl
          res.apr shouldEqual exp.apr
          res.features shouldEqual exp.features
          res.cardScore shouldEqual (exp.cardScore +- 0.001)
        }
    }
  }

}
