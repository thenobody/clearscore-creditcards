lazy val creditCardsCore = Projects.creditCardsCore
lazy val creditCardsService = Projects.creditCardsService

lazy val root = project
  .in(file("."))
  .settings(name := "credit-cards")
  .aggregate(creditCardsCore, creditCardsService)