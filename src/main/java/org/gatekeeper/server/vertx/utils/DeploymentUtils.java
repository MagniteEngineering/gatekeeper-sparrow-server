package org.gatekeeper.server.vertx.utils;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import org.gatekeeper.server.vertx.Deployer;

import java.util.Iterator;
import java.util.List;

public class DeploymentUtils {
    private DeploymentUtils() {
    }

    public static Future<Void> sequential(List<Deployer> deployers) {
        Promise<Void> promise = Promise.promise();
        deploy(deployers.iterator(), promise);
        return promise.future();
    }

    private static void deploy(Iterator<Deployer> iterator, Promise<Void> result) {
        if (!iterator.hasNext()) {
            result.complete();
            return;
        }
        Deployer next = iterator.next();

        next.deploy().onComplete(res -> {
            if (res.failed()) {
                result.fail(res.cause());
            } else {
                deploy(iterator, result);
            }
        });
    }
}
