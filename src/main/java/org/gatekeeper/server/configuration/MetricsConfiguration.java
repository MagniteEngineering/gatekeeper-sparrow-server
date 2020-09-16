package org.gatekeeper.server.configuration;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.ScheduledReporter;
import com.codahale.metrics.SharedMetricRegistries;
import com.codahale.metrics.graphite.Graphite;
import com.codahale.metrics.graphite.GraphiteReporter;
import org.gatekeeper.server.configuration.properties.GraphiteProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfiguration {

    private static final String METRIC_REGISTRY_NAME = "metric-registry";

    @Bean
    MetricRegistry metricRegistry() {
        return SharedMetricRegistries.getOrCreate(METRIC_REGISTRY_NAME);
    }

    @Bean
    @ConfigurationProperties(prefix = "metrics.graphite")
    GraphiteProperties graphiteProperties() {
        return new GraphiteProperties();
    }

    @Bean
    @ConditionalOnProperty(prefix = "metrics.graphite", name = "enabled", havingValue = "true")
    ScheduledReporter graphiteReporter(GraphiteProperties graphiteProperties, MetricRegistry metricRegistry) {
        Graphite graphite = new Graphite(graphiteProperties.getHost(), graphiteProperties.getPort());
        ScheduledReporter reporter = GraphiteReporter.forRegistry(metricRegistry)
                .prefixedWith(graphiteProperties.getPrefix())
                .build(graphite);
        reporter.start(graphiteProperties.getInterval(), graphiteProperties.getIntervalUnit());

        return reporter;
    }
}
