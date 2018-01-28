package net.thenobody.clearscore.creditcards.service

import akka.http.scaladsl.Http
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.StrictLogging
import net.thenobody.clearscore.creditcards.service.context.Context

import scala.util.{Failure, Success}

object Main extends StrictLogging {

  def main(args: Array[String]): Unit = {
    val context = Context.load(ConfigFactory.load())
    import context._
    Http()
      .bindAndHandle(rootRoute.build, context.serviceInterface, context.servicePort)
      .onComplete {
        case Success(binding) =>
          logger.info(s"Server successfully started on ${binding.localAddress}")
        case Failure(reason) =>
          logger.error(s"Server failed to start", reason)
      }
  }

}
