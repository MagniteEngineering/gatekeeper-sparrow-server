package org.gatekeeper.server.handler;

import io.vertx.core.Handler;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import org.gatekeeper.server.admin.service.WebSocketBroker;

import java.text.MessageFormat;

@Slf4j
public class RequestLogHandler implements Handler<RoutingContext> {

    private static final Logger REQUEST_LOGGER = LoggerFactory.getLogger("http-request-logger");

    private WebSocketBroker webSocketBroker;

    public RequestLogHandler(WebSocketBroker webSocketBroker) {
        this.webSocketBroker = webSocketBroker;
    }

    @Override
    public void handle(RoutingContext event) {
        event.next();
        var message = MessageFormat.format("Uri: \"{0}\". Body: \"{1}\".", event.request().uri(), event.getBody());
        webSocketBroker.sendLogMessage(message);
        REQUEST_LOGGER.debug(message);
    }
}
