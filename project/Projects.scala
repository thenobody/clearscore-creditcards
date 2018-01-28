import com.typesafe.sbt.packager.archetypes.JavaAppPackaging
import sbt._
import sbt.Keys._

object Projects {
  lazy val creditCardsCore = Project("credit-cards-core", file("credit-cards-core"))
    .settings(Common.commonSettings)
    .settings(
      libraryDependencies ++= Seq(
        "org.scala-lang" % "scala-library" % scalaVersion.value,
        "org.scala-lang" % "scala-reflect" % scalaVersion.value
      ) ++ Dependencies.CommonDeps ++ Dependencies.CreditCardsCoreDeps
    )

  lazy val creditCardsService = Project("credit-cards-service", file("credit-cards-service"))
    .settings(Common.commonSettings)
    .dependsOn(creditCardsCore % "compile->compile;test->test")
    .settings(
      libraryDependencies ++= Seq(
        "org.scala-lang" % "scala-library" % scalaVersion.value,
        "org.scala-lang" % "scala-reflect" % scalaVersion.value
      ) ++ Dependencies.CommonDeps ++ Dependencies.CreditCardsServiceDeps
    )
    .enablePlugins(JavaAppPackaging)
}