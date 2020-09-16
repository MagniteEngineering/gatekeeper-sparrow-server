package org.gatekeeper.server.handler;

import io.vertx.core.Handler;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RequestLogHandler implements Handler<RoutingContext> {

    private static final Logger REQUEST_LOGGER = LoggerFactory.getLogger("http-request-logger");

    @Override
    public void handle(RoutingContext event) {
        event.next();
        REQUEST_LOGGER.info(
                "Uri: \"{0}\". Body: \"{1}\".",
                event.request().uri(),
                event.getBody().toString());
    }
}
