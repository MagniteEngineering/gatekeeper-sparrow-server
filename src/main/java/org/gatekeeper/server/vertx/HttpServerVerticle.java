package org.gatekeeper.server.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpServerVerticle extends AbstractVerticle {
    private HttpServerOptions options;
    private Router router;

    public HttpServerVerticle(HttpServerOptions options, Router router) {
        this.options = options;
        this.router = router;
    }

    @Override
    public void start() {
        log.debug("Starting http server instance on port {}", options.getPort());
        vertx.createHttpServer(options).requestHandler(router).listen();
    }

}
