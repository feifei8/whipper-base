package filters

import play.api.Logger
import play.api.mvc._
import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits.defaultContext

object LoggingFilter extends Filter {
    def apply(nextFilter: (RequestHeader) => Future[Result])
            (requestHeader: RequestHeader): Future[Result] = {
        val startTime = System.currentTimeMillis
        nextFilter(requestHeader).map { result =>
            val endTime = System.currentTimeMillis
            val requestTime = endTime - startTime
            val msg = s"${requestHeader.method} ${requestHeader.uri} " +
                    s"took ${requestTime}ms and returned ${result.header.status}"
            play.Logger.of("access").info(msg)
            // Logger.info(msg)
            result.withHeaders("Request-Time" -> requestTime.toString)
        }
    }
}
