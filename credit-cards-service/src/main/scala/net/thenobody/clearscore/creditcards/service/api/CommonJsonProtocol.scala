package net.thenobody.clearscore.creditcards.service.api

import spray.json._

object CommonJsonProtocol extends DefaultJsonProtocol {

  implicit val requestReader: RootJsonFormat[ApiRequest] = jsonFormat6(ApiRequest.apply)
}
