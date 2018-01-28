package net.thenobody.clearscore.creditcards.service.context

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.typesafe.config.Config
import net.thenobody.clearscore.creditcards.core.service.CardsService
import net.thenobody.clearscore.creditcards.core.service.cscards.{CSCardsClient, CSCardsClientImpl, CSCardsService}
import net.thenobody.clearscore.creditcards.core.service.scoredcards.{ScoredCardsClient, ScoredCardsClientImpl, ScoredCardsService}
import net.thenobody.clearscore.creditcards.service.route.RootRoute

import scala.concurrent.ExecutionContext

trait Context {
  def appConfig: Config
  implicit def actorSystem: ActorSystem
  implicit def executionContext: ExecutionContext
  implicit def actorMaterializer: ActorMaterializer
  def serviceInterface: String
  def servicePort: Int
  def csCardsClient: CSCardsClient
  def csCardsService: CSCardsService
  def scoredCardsClient: ScoredCardsClient
  def scoredCardsService: ScoredCardsService
  def cardServices: Seq[CardsService]

  def rootRoute: RootRoute
}

object Context {
  def load(config: Config): Context = new Context {
    implicit val actorSystem: ActorSystem = ActorSystem()
    implicit val executionContext: ExecutionContext = actorSystem.dispatcher
    implicit val actorMaterializer: ActorMaterializer = ActorMaterializer()

    val appConfig: Config = config

    val serviceInterface: String = config.getString("credit-cards.service.interface")
    val servicePort: Int = config.getInt("credit-cards.service.port")

    val csCardsClient: CSCardsClient = CSCardsClientImpl(config)
    val csCardsService: CSCardsService = new CSCardsService(csCardsClient.execute)

    val scoredCardsClient: ScoredCardsClient = ScoredCardsClientImpl(config)
    val scoredCardsService: ScoredCardsService = new ScoredCardsService(scoredCardsClient.execute)

    val cardServices: Seq[CardsService] = Seq(csCardsService, scoredCardsService)

    val rootRoute: RootRoute = new RootRoute(cardServices)
  }
}
