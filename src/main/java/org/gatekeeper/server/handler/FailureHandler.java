package org.gatekeeper.server.handler;

import io.netty.handler.codec.http.HttpHeaderValues;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import org.gatekeeper.server.handler.model.Response;
import org.gatekeeper.server.handler.utils.HttpUtils;
import org.gatekeeper.server.json.JacksonMapper;

@Slf4j
public class FailureHandler implements Handler<RoutingContext> {

    private JacksonMapper mapper;

    public FailureHandler(JacksonMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void handle(RoutingContext event) {
        log.debug("Internal error. Please fix");
        event.response()
                .putHeader(HttpUtils.CONTENT_TYPE_HEADER, HttpHeaderValues.APPLICATION_JSON)
                .end(mapper.encode(Response.fail(event.failure().toString())));
    }

}
