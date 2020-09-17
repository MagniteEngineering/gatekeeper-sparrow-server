package org.gatekeeper.server.handler.utils;

import io.vertx.core.http.HttpHeaders;

public final class HttpUtils {
    public static final CharSequence CONTENT_TYPE_HEADER = HttpHeaders.createOptimized("Content-Type");

    private HttpUtils() {
    }

}
