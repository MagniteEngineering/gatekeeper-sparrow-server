package org.gatekeeper.server.configuration;

import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;
import org.gatekeeper.server.vertx.CompositeDeployer;
import org.gatekeeper.server.vertx.Deployer;
import org.gatekeeper.server.vertx.HttpServerVerticle;
import org.gatekeeper.server.vertx.VerticleDeployer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.function.Supplier;

@Configuration
public class HttpServerConfiguration {
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
        return new VerticleDeployer("http server", vert, supplier);
    }

    @Bean
    CompositeDeployer compositeDeployer(List<Deployer> deployers) {
        return new CompositeDeployer(deployers);
    }

}
