package net.thenobody.clearscore.creditcards.core.service.cscards

import net.thenobody.clearscore.client.cscardsswagger.model.Card
import net.thenobody.clearscore.creditcards.ModelGenerators._
import net.thenobody.clearscore.creditcards.core.model.CreditCard
import org.scalacheck.Gen
import org.scalatest.prop.PropertyChecks
import org.scalatest.{FlatSpec, Matchers}

class CardSearchResponseOpsTest extends FlatSpec with Matchers with PropertyChecks {

  behavior of classOf[CardSearchResponseOps].getSimpleName

  it should "convert a CardSearchResponse into a collection of CreditCard with correct score" in {
    forAll(aCardName, anUri, anAPR, anEligibilityRating, Gen.option(aStringList)) {
      (card, uri, apr, eligibility, features) =>

        val score = eligibility / 10.0 * math.pow(apr / 100.0, -2) / 100
        val csCard = Card(card, uri.toString, apr, eligibility, features)
        val input = Seq(csCard)

        val creditCard = CreditCard("CSCards", card, uri, apr, features.getOrElse(Seq.empty), score)
        val expResult = Seq(creditCard)

        val result = new CardSearchResponseOps(input).toResponse

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
