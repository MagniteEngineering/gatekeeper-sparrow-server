package org.gatekeeper.server;

import lombok.extern.slf4j.Slf4j;
import org.gatekeeper.server.vertx.CompositeDeployer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.StopWatch;

import javax.annotation.PostConstruct;

@SpringBootApplication
@Slf4j
public class Application {

    private CompositeDeployer compositeDeployer;

    public Application(CompositeDeployer compositeDeployer) {
        this.compositeDeployer = compositeDeployer;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @PostConstruct
    void init() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        compositeDeployer.deploy()
                .onComplete(result -> {
                    stopWatch.stop();
                    if (result.failed()) {
                        throw new IllegalStateException(result.cause());
                    }
                    log.debug("Started vertx context in {}s", stopWatch.getTotalTimeMillis() / 1000.0);
                });
    }
}
