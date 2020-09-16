package org.gatekeeper.server.vertx;

import io.vertx.core.Future;

@FunctionalInterface
public interface Deployer {
    Future<Void> deploy();
}
