package org.gatekeeper.server.handler;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import org.gatekeeper.server.handler.utils.HttpUtils;

@Slf4j
public class NoCacheHandler implements Handler<RoutingContext> {

    @Override
    public void handle(RoutingContext context) {
        context.response()
                .putHeader(HttpUtils.CACHE_CONTROL_HEADER, "no-cache");
        context.next();
    }

}
