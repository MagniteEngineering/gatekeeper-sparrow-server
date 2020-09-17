package org.gatekeeper.server.configuration;

import com.codahale.metrics.MetricRegistry;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.ext.dropwizard.DropwizardMetricsOptions;
import org.gatekeeper.server.json.JacksonMapper;
import org.gatekeeper.server.json.ObjectMapperProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VertxConfiguration {

    @Bean
    JacksonMapper jacksonMapper() {
        return new JacksonMapper(ObjectMapperProvider.mapper());
    }

    @Bean
    VertxOptions vertxOptions(MetricRegistry metricRegistry) {
        DropwizardMetricsOptions dropwizardMetricsOptions = new DropwizardMetricsOptions()
                .setMetricRegistry(metricRegistry)
                .setEnabled(true);

        return new VertxOptions().setMetricsOptions(dropwizardMetricsOptions);
    }

    @Bean
    Vertx vertx(VertxOptions vertxOptions) {
        return Vertx.vertx(vertxOptions);
    }
}
