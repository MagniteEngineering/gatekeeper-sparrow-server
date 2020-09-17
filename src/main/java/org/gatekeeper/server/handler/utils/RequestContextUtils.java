package org.gatekeeper.server.handler.utils;

import io.vertx.ext.web.RoutingContext;

public class RequestContextUtils {
    public static final String REQUEST_CONTEXT = "request-context";

    private RequestContextUtils() {
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(RoutingContext context, Class<T> clazz) {
        return (T) context.data().get(REQUEST_CONTEXT + clazz.getSimpleName());
    }

    public static <T> void put(RoutingContext context, Class<T> clazz, T data) {
        context.data().put(REQUEST_CONTEXT + clazz.getSimpleName(), data);
    }

}
