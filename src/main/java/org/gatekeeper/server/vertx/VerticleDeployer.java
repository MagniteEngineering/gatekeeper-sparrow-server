package org.gatekeeper.server.vertx;

import io.vertx.core.*;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Supplier;

@Slf4j
public class VerticleDeployer implements Deployer {
    private Vertx vertx;
    private Supplier<Verticle> supplier;
    private DeploymentOptions options;
    private String name;

    public VerticleDeployer(String name, Vertx vertx, Supplier<Verticle> supplier) {
        this(name, vertx, supplier, new DeploymentOptions());
    }

    public VerticleDeployer(String name, Vertx vertx, Supplier<Verticle> supplier, DeploymentOptions options) {
        this.vertx = vertx;
        this.supplier = supplier;
        this.options = options;
        this.name = name;
    }

    @Override
    public Future<Void> deploy() {
        Promise<Void> promise = Promise.promise();
        vertx.deployVerticle(supplier,
                options,
                result -> {
                    if (result.succeeded()) {
                        log.info("[{}] Started. Instances: {}. Worker: {} ",
                                name, options.getInstances(), options.isWorker());
                        promise.complete();
                        return;
                    }

                    log.error("Failed to deploy verticle {}", name);
                    promise.fail(result.cause());
                    throw new IllegalStateException("Failed to deploy verticle", result.cause());
                });
        return promise.future();
    }
}
