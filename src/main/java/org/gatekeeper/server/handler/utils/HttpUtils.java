package org.gatekeeper.server.handler.utils;

import io.vertx.core.http.HttpHeaders;

public final class HttpUtils {

    public static final CharSequence CACHE_CONTROL_HEADER = HttpHeaders.createOptimized("Cache-Control");
    public static final CharSequence CONTENT_TYPE_HEADER = HttpHeaders.createOptimized("Content-Type");
    public static final CharSequence ORIGIN_HEADER = HttpHeaders.createOptimized("origin");

    private HttpUtils() {
    }

}
