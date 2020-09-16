package org.gatekeeper.server.configuration;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;
import lombok.extern.slf4j.Slf4j;
import org.gatekeeper.server.handler.RequestLogHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Configuration
public class RouteConfiguration {

    @SuppressWarnings({"unchecked"})
    @Bean
    Map<String, Handler<RoutingContext>> handlerRegistry(
            ApplicationContext applicationContext) {
        String[] beanNamesForType = applicationContext.getBeanNamesForType(Handler.class);
        return Arrays.stream(beanNamesForType)
                .collect(Collectors.toMap(
                        Function.identity(),
                        bean -> (Handler<RoutingContext>) applicationContext.getBean(bean, Handler.class)));
    }

    @Bean
    Router router(
            Vertx vertx,
            Map<String, Handler<RoutingContext>> handlerRegistry) {
        Router router = Router.router(vertx);

        router.route().handler(BodyHandler.create());

        router.route("/sparrow/api/v1/*")
                .handler(handlerRegistry.get("requestLogHandler"));

        router.get("/webroot/*").handler(handlerRegistry.get("staticHandler"));
        router.get("/").handler(handlerRegistry.get("staticHandler"));

        return router;
    }

    @Bean
    StaticHandler staticHandler() {
        return StaticHandler.create();
    }

    @Bean
    RequestLogHandler requestLogHandler() {
        return new RequestLogHandler();
    }

}
