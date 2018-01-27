import sbt.Keys._

object Common {
  lazy val commonSettings = Seq(
    version := "0.0.1",
    scalaVersion := "2.12.4",
    organization := "net.thenobody.clearscore",
    javacOptions ++= Seq(
      "-source", "1.8",
      "-target", "1.8"
    )
  )
}