package net.thenobody.clearscore.creditcards

import java.net.URI

import com.github.nscala_time.time.Imports._
import net.thenobody.clearscore.creditcards.core.model.{CreditCard, EmploymentStatus}
import org.scalacheck.Gen

trait ModelGenerators {

  def aFirstName: Gen[String] = Gen.oneOf(
    "Leota",
    "Major",
    "Shena",
    "Marta",
    "Marvis",
    "Jenniffer",
    "Christiane",
    "Therese",
    "Alexis",
    "Erika",
    "Fausto",
    "Beau",
    "Kevin",
    "Ramiro",
    "Delmer",
    "Augustina",
    "Ayanna",
    "Michell",
    "Anastasia",
    "Adriene"
  )

  def aLastName: Gen[String] = Gen.oneOf(
    "Ballengee",
    "Neider",
    "Lucien",
    "Astudillo",
    "Wiltse",
    "Guillen",
    "Abernethy",
    "Horak",
    "Rayborn",
    "Lipp",
    "Deegan",
    "Stoneham",
    "Schanz",
    "Wingard",
    "Mcmahon",
    "Wilkin",
    "Lawton",
    "Flegle",
    "Siler",
    "Farwell"
  )
  def aDateTime: Gen[DateTime] = Gen.choose(0, 9999999).map(DateTime.now + _)
  def aDate: Gen[DateTime] =
    Gen
      .choose(0, 9999999)
      .map(DateTime.now + _.minutes)
      .map(_.withMillisOfDay(0))
  def anEmploymentStatus: Gen[EmploymentStatus] =
    Gen.oneOf(EmploymentStatus.values)
  def anEmploymentStatusString: Gen[String] =
    Gen.oneOf(
      "FULL_TIME",
      "PART_TIME",
      "STUDENT",
      "UNEMPLOYED",
      "RETIRED",
    )
  def aCreditScore: Gen[Int] = Gen.choose(0, 700)
  def aSalary: Gen[Int] = Gen.choose(0, 10000)
  def aCardName: Gen[String] = Gen.alphaStr
  def aUri: Gen[URI] = Gen.oneOf(
    new URI("http://not-secure.local/path/to/resource"),
    new URI("https://secure.local:443/path/to/resource"),
    new URI("https://subdomain-of.secure.local/path/to/resource"),
    new URI("https://username:password@secure.local/path/to/resource"),
  )
  def anApprovalRating: Gen[Double] = Gen.choose(0.0, 1.0)
  def anEligibilityRating: Gen[Double] = Gen.choose(0.0, 10.0)
  def aCardScore: Gen[Double] = Gen.posNum[Double]
  def anAPR: Gen[Double] = Gen.choose(50.0, 100.0)
  def aStringList: Gen[List[String]] = Gen.listOf(Gen.alphaStr)

  def aProvider: Gen[String] = Gen.oneOf("CSCards", "ScoredCards")

  def aCreditCard: Gen[CreditCard] = for {
    provider <- aProvider
    name <- aCardName
    applyUrl <- aUri
    apr <- anAPR
    features = Seq("asdasda")
    cardScore <- aCardScore
  } yield CreditCard(provider, name, applyUrl, apr, features, cardScore)
}

object ModelGenerators extends ModelGenerators
