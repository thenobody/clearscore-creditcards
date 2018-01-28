package net.thenobody.clearscore.creditcards.core.service.scoredcards

import net.thenobody.clearscore.creditcards.core.model.scoredcards.CreditCard
import org.scalatest.{FlatSpec, Matchers}

class CreditCardOpsTest extends FlatSpec with Matchers {

  behavior of classOf[CreditCardOps].getSimpleName

  it should "compute the correct credit score" in {
    val input = CreditCard("", "", 19.4, 0.8, Seq(), Seq())
    val expResult = 0.212

    val result = new CreditCardOps(input).normalisedScore

    result shouldEqual (expResult +- 0.001)
  }

}
