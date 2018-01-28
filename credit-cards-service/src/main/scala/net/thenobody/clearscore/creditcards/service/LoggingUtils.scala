package net.thenobody.clearscore.creditcards.service

import akka.event.Logging
import akka.event.Logging.LogLevel
import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.server.directives.{DebuggingDirectives, LogEntry, LoggingMagnet}
import akka.http.scaladsl.server.{Route, RouteResult}

trait LoggingUtils {
  val loggingMagnet: LoggingMagnet[HttpRequest => RouteResult => Unit] = LoggingMagnet { log =>
    request => {
      case RouteResult.Complete(response) =>
        LogEntry(s"${request.method} ${request.uri.toRelative} - HTTP ${response.status}", Logging.InfoLevel).logTo(log)
      case RouteResult.Rejected(rejections) =>
        LogEntry(s"${request.method} ${request.uri.toRelative} - ${rejections.mkString(", ")}", Logging.ErrorLevel).logTo(log)
    }
  }

  def logRequestResult(logLevel: LogLevel)(route: Route): Route = DebuggingDirectives.logRequestResult(loggingMagnet)(route)

}
