package org.gatekeeper.server.vertx.utils;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import lombok.extern.slf4j.Slf4j;
import org.gatekeeper.server.vertx.Deployer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
class DeploymentUtilsTest {

    @Test
    void sequential() {
        log.debug("All 3 must complete");
        TestDeployer deployer1 = new TestDeployer("1", true, 10);
        TestDeployer deployer2 = new TestDeployer("2", true, 20);
        TestDeployer deployer3 = new TestDeployer("3", true, 30);
        Future<Void> voidFuture = DeploymentUtils.sequential(
                Arrays.asList(deployer1, deployer2, deployer3));
        awaitCompletion(voidFuture);
        Assertions.assertTrue(deployer1.succeeded());
        Assertions.assertTrue(deployer2.succeeded());
        Assertions.assertTrue(deployer3.succeeded());
    }

    private static class TestDeployer implements Deployer {
        private ExecutorService service = Executors.newSingleThreadExecutor();
        private String name;
        private Boolean isSucceeded;
        private boolean isSucceeding;
        private int delay;
        private int priority;

        TestDeployer(String name, boolean isSucceeding, int delay, int priority) {
            this.name = name;
            this.isSucceeding = isSucceeding;
            this.delay = delay;
            this.priority = priority;
        }

        TestDeployer(String name, boolean isSucceeding, int delay) {
            this(name, isSucceeding, delay, 1);
        }

        @Override
        public Future<Void> deploy() {
            Promise<Void> promise = Promise.promise();
            Promise<Void> result = Promise.promise();

            service.execute(() -> this.handlePromise(promise));

            promise.future().onComplete(res -> {
                isSucceeded = res.succeeded();
                result.handle(res);
            });

            return result.future();
        }

        void handlePromise(Promise<Void> promise) {
            log.debug("Deploying {} with priority {}", name, priority);
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                promise.fail(e);
            }

            if (isSucceeding) {
                log.debug("Completed deploying {}", name);
                promise.complete();
            } else {
                log.debug("Failing deploying {}", name);
                promise.fail("Failure");
            }
        }

        Boolean succeeded() {
            return isSucceeded;
        }

    }

    private void awaitCompletion(Future<Void> future) {
        Promise<Void> promise = Promise.promise();
        future.onComplete(res -> {
            promise.complete();
        });
        while (!promise.future().isComplete()) {
            try {
                Thread.sleep(10);
            } catch (Exception e) {
                // ignored
            }
        }
        log.debug("Completed");
    }
}
