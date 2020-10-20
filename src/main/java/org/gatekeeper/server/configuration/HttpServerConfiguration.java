package org.gatekeeper.server.configuration;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.impl.cpu.CpuCoreSensor;
import io.vertx.ext.web.Router;
import org.gatekeeper.server.vertx.CompositeDeployer;
import org.gatekeeper.server.vertx.Deployer;
import org.gatekeeper.server.vertx.HttpServerVerticle;
import org.gatekeeper.server.vertx.VerticleDeployer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Configuration
public class HttpServerConfiguration {
    private static final int DEFAULT_INSTANCES = CpuCoreSensor.availableProcessors() * 2;

    @ConfigurationProperties(prefix = "vertx.http-server")
    @Bean
    HttpServerOptions httpServerOptions() {
        return new HttpServerOptions();
    }

    @Bean
    Deployer httpServerDeployer(
            Vertx vert,
            HttpServerOptions httpServerOptions,
            Router router) {
        Supplier<Verticle> supplier = () -> new HttpServerVerticle(httpServerOptions, router);
        DeploymentOptions options = new DeploymentOptions()
                .setWorker(true)
                .setWorkerPoolName("vertx-pool-http")
                .setWorkerPoolSize(DEFAULT_INSTANCES)
                .setMaxWorkerExecuteTime(20)
                .setMaxWorkerExecuteTimeUnit(TimeUnit.SECONDS)
                .setInstances(DEFAULT_INSTANCES);
        return new VerticleDeployer("http server", vert, supplier, options);
    }

    @Bean
    CompositeDeployer compositeDeployer(List<Deployer> deployers) {
        return new CompositeDeployer(deployers);
    }

}
