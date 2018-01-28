package net.thenobody.clearscore.creditcards.service.route

import akka.http.scaladsl.model.StatusCodes
import com.github.nscala_time.time.Imports._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import net.thenobody.clearscore.creditcards.core.model.{CreditCard, EmploymentStatus, Request}
import net.thenobody.clearscore.creditcards.core.service.CardsService
import net.thenobody.clearscore.creditcards.service.api.{ApiCreditCard, ApiRequest}
import org.scalacheck.Gen
import org.scalatest.prop.PropertyChecks
import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.Future
import net.thenobody.clearscore.creditcards.ModelGenerators._
import net.thenobody.clearscore.creditcards.service.api.JsonProtocol._
import spray.json._

class RootRouteTest
    extends FlatSpec
    with Matchers
    with ScalatestRouteTest
    with PropertyChecks {

  behavior of classOf[RootRoute].getSimpleName

  val anApiRequest: Gen[ApiRequest] = for {
    firstName <- aFirstName
    lastName <- aLastName
    dateOfBirth <- aDate.map(_.toString("yyyy/MM/dd"))
    creditScore <- aCreditScore
    employmentStatus <- anEmploymentStatus
    salary <- aSalary
  } yield ApiRequest(firstName, lastName, dateOfBirth, creditScore, employmentStatus.entryName, salary)

  it should "retrieve empty credit cards list" in {
    forAll(anApiRequest) { apiRequest =>
        val services = Seq.empty
        val route = new RootRoute(services).build

        val expResult = Seq.empty

        Post("/creditcards", apiRequest) ~> route ~> check {
          responseAs[Seq[ApiCreditCard]] shouldEqual expResult
        }
    }
  }

  it should "retrieve sorted credit cards list" in {
    forAll(anApiRequest) { apiRequest =>
        val request = Request(
          apiRequest.firstName,
          apiRequest.lastName,
          DateTime.parse(apiRequest.dob, DateTimeFormat.forPattern("yyyy/MM/dd")),
          apiRequest.score,
          EmploymentStatus.withName(apiRequest.employmentStatus),
          apiRequest.salary
        )

        val creditCards1 =
          Gen.listOf(aCreditCard).sample.getOrElse(Seq.empty)
        val creditCards2 =
          Gen.listOf(aCreditCard).sample.getOrElse(Seq.empty)

        val apiCreditCards1 = creditCards1.map {
          case CreditCard(provider, name, url, ap, f, sc) =>
            ApiCreditCard(
              provider,
              name,
              url.toString,
              BigDecimal(ap).setScale(2, BigDecimal.RoundingMode.HALF_UP),
              f,
              BigDecimal(sc).setScale(3, BigDecimal.RoundingMode.HALF_UP))
        }

        val apiCreditCards2 = creditCards2.map {
          case CreditCard(provider, name, url, ap, f, sc) =>
            ApiCreditCard(
              provider,
              name,
              url.toString,
              BigDecimal(ap).setScale(2, BigDecimal.RoundingMode.HALF_UP),
              f,
              BigDecimal(sc).setScale(3, BigDecimal.RoundingMode.HALF_UP))
        }

        val mockService1 = new CardsService {
          def getCardsByRequest(r: Request): Future[Seq[CreditCard]] = r match {
            case `request` =>
              Future.successful(creditCards1)
            case _ =>
              Future.failed(new Exception("Invalid request"))
          }
        }

        val mockService2 = new CardsService {
          def getCardsByRequest(r: Request): Future[Seq[CreditCard]] = r match {
            case `request` =>
              Future.successful(creditCards2)
            case _ =>
              Future.failed(new Exception("Invalid request"))
          }
        }
        val services = Seq(mockService1, mockService2)
        val route = new RootRoute(services).build

        val expResult =
          (apiCreditCards1 ++ apiCreditCards2).sortBy(_.cardScore).reverse

        Post("/creditcards", apiRequest) ~> route ~> check {
          responseAs[Seq[ApiCreditCard]] shouldEqual expResult
        }
    }
  }

  it should "handle missing parameters" in {
    forAll(anApiRequest) { apiRequest =>
      val request = Map(
        "firstname" -> JsString(apiRequest.firstName),
        "lastname" -> JsString(apiRequest.lastName),
        "dob" -> JsString(apiRequest.dob),
        "credit-score" -> JsNumber(apiRequest.score),
        "employment-status" -> JsString(apiRequest.employmentStatus),
        "salary" -> JsNumber(apiRequest.salary)
      )
      val services = Seq.empty
      val route = new RootRoute(services).build

      Post("/creditcards", JsObject(request - "firstname")) ~> route ~> check {
        response.status shouldEqual StatusCodes.BadRequest
        responseAs[Map[String, String]] shouldEqual Map("error" -> "Object is missing required member 'firstname'")
      }

      Post("/creditcards", JsObject(request - "lastname")) ~> route ~> check {
        response.status shouldEqual StatusCodes.BadRequest
        responseAs[Map[String, String]] shouldEqual Map("error" -> "Object is missing required member 'lastname'")
      }
      Post("/creditcards", JsObject(request - "dob")) ~> route ~> check {
        response.status shouldEqual StatusCodes.BadRequest
        responseAs[Map[String, String]] shouldEqual Map("error" -> "Object is missing required member 'dob'")
      }
      Post("/creditcards", JsObject(request - "credit-score")) ~> route ~> check {
        response.status shouldEqual StatusCodes.BadRequest
        responseAs[Map[String, String]] shouldEqual Map("error" -> "Object is missing required member 'credit-score'")
      }
      Post("/creditcards", JsObject(request - "employment-status")) ~> route ~> check {
        response.status shouldEqual StatusCodes.BadRequest
        responseAs[Map[String, String]] shouldEqual Map("error" -> "Object is missing required member 'employment-status'")
      }
      Post("/creditcards", JsObject(request - "salary")) ~> route ~> check {
        response.status shouldEqual StatusCodes.BadRequest
        responseAs[Map[String, String]] shouldEqual Map("error" -> "Object is missing required member 'salary'")
      }
    }
  }

  it should "reject invalid credit score" in {
    forAll(anApiRequest) { request =>
      val services = Seq.empty
      val route = new RootRoute(services).build

      Post("/creditcards", request.copy(score = -1)) ~> route ~> check {
        response.status shouldEqual StatusCodes.BadRequest
        responseAs[Map[String, String]] shouldEqual Map("error" -> "InvalidCreditScore")
      }
      Post("/creditcards", request.copy(score = 701)) ~> route ~> check {
        response.status shouldEqual StatusCodes.BadRequest
        responseAs[Map[String, String]] shouldEqual Map("error" -> "InvalidCreditScore")
      }
    }
  }

  it should "reject invalid date of birth" in {
    forAll(anApiRequest) { request =>
      val services = Seq.empty
      val route = new RootRoute(services).build

      Post("/creditcards", request.copy(dob = "INVALID")) ~> route ~> check {
        response.status shouldEqual StatusCodes.BadRequest
        responseAs[Map[String, String]] shouldEqual Map("error" -> "InvalidDateOfBirth")
      }
    }
  }

  it should "reject invalid employment status" in {
    forAll(anApiRequest) { request =>
      val services = Seq.empty
      val route = new RootRoute(services).build

      Post("/creditcards", request.copy(employmentStatus = "INVALID")) ~> route ~> check {
        response.status shouldEqual StatusCodes.BadRequest
        responseAs[Map[String, String]] shouldEqual Map("error" -> "InvalidEmploymentStatus(INVALID)")
      }
    }
  }

  it should "reject invalid salary" in {
    forAll(anApiRequest) { request =>
      val services = Seq.empty
      val route = new RootRoute(services).build

      Post("/creditcards", request.copy(salary = -1)) ~> route ~> check {
        response.status shouldEqual StatusCodes.BadRequest
        responseAs[Map[String, String]] shouldEqual Map("error" -> "InvalidSalary")
      }
    }
  }

  it should "respond to ping" in {
    val services = Seq.empty
    val route = new RootRoute(services).build

    Get("/ping") ~> route ~> check {
      response.status shouldEqual StatusCodes.OK
      responseAs[String] shouldEqual "pong"
    }
  }
}
