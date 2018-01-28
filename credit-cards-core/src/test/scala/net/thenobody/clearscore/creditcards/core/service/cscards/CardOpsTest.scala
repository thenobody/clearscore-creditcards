package net.thenobody.clearscore.creditcards.core.service.cscards

import net.thenobody.clearscore.creditcards.core.model.cscards.Card
import org.scalatest.{FlatSpec, Matchers}

class CardOpsTest extends FlatSpec with Matchers {

  behavior of classOf[CardOps].getSimpleName

  it should "compute the correct credit score" in {
    val input = Card("", "", 21.4, 6.3, None)
    val expResult = 0.137

    val result = new CardOps(input).normalisedScore

    result shouldEqual (expResult +- 0.001)
  }

}
