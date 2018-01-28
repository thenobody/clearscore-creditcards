import sbt._

object Dependencies {
  object Versions {
    val Akka = "2.5.9"
    val AkkaHttp = "10.0.11"
    val Cats = "0.9.0"
    val Client = "1.0.0"
    val Enumeratum = "1.5.12"
    val Logback = "1.2.3"

    val ScalaCheck = "1.13.5"
    val ScalaTime = "2.18.0"
    val ScalaTest = "3.0.4"
    val Slf4J = "1.7.25"
    val TypesafeConfig = "1.3.1"
  }

  object Groups {
    val Akka = "com.typesafe.akka"
    val Client = "net.thenobody.clearscore.client"
    val Slf4J = "org.slf4j"
  }

  lazy val akka = Groups.Akka %% "akka-actor" % Versions.Akka
  lazy val akkaSlf4J = Groups.Akka %% "akka-slf4j" % Versions.Akka
  lazy val akkaHttp = Groups.Akka %% "akka-http" % Versions.AkkaHttp
  lazy val akkaHttpSpray = Groups.Akka %% "akka-http-spray-json" % Versions.AkkaHttp

  lazy val cats = "org.typelevel" %% "cats" % Versions.Cats

  lazy val enumeratum = "com.beachape" %% "enumeratum" % Versions.Enumeratum
  lazy val logback = "ch.qos.logback" % "logback-classic" % Versions.Logback
  lazy val scalaTime = "com.github.nscala-time" %% "nscala-time" % Versions.ScalaTime
  lazy val slf4j = "org.slf4j" % "slf4j-api" % Versions.Slf4J
  lazy val typesafeConfig = "com.typesafe" % "config" % Versions.TypesafeConfig

  // generated swagger code
  lazy val csCards = Groups.Client %% "cscards-swagger" % Versions.Client
  lazy val scoredCards = Groups.Client %% "scoredcards-swagger" % Versions.Client
  lazy val microservice = Groups.Client %% "microservice-swagger" % Versions.Client

  // test scope
  lazy val scalaCheck = "org.scalacheck" %% "scalacheck" % Versions.ScalaCheck % Test
  lazy val scalaTest = "org.scalatest" %% "scalatest" % Versions.ScalaTest % Test

  val CommonDeps = Seq(
    cats,
    enumeratum,
    logback,
    slf4j,
    typesafeConfig,
    scalaCheck,
    scalaTime,
    scalaTest
  )

  val CreditCardsServiceDeps = Seq(
    akka,
    akkaSlf4J,
    akkaHttp,
    akkaHttpSpray,
    microservice
  )

  val CreditCardsCoreDeps = Seq(
    csCards,
    scoredCards
  )
}