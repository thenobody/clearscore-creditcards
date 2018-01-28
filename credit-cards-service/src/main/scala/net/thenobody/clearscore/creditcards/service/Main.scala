package net.thenobody.clearscore.creditcards.service

import akka.http.scaladsl.Http
import com.typesafe.config.ConfigFactory
import net.thenobody.clearscore.creditcards.service.context.Context

object Main {

  def main(args: Array[String]): Unit = {
    val context = Context.load(ConfigFactory.load())
    import context._
    Http()
      .bindAndHandle(rootRoute.build, context.serviceInterface, context.servicePort)
  }

}
