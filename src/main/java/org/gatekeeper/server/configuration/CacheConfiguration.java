package org.gatekeeper.server.configuration;

import org.gatekeeper.server.handler.ad.model.AdRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.Cache;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;
import javax.cache.spi.CachingProvider;
import java.util.concurrent.TimeUnit;

@EnableCaching
@Configuration
public class CacheConfiguration {

    @Bean
    CachingProvider cachingProvider() {
        return Caching.getCachingProvider();
    }

    @Bean
    Cache<String, AdRequest> adRequestCache(
            CachingProvider cachingProvider,
            @Value("${cache.ad-requests-cache.durationAmount:1}") int duration,
            @Value("${cache.ad-requests-cache.durationTimeUnit:SECONDS}") TimeUnit durationUnit) {

        var expiryPolicy = CreatedExpiryPolicy.factoryOf(new Duration(durationUnit, duration));
        var configuration = new MutableConfiguration<String, AdRequest>()
                .setTypes(String.class, AdRequest.class)
                .setStoreByValue(false)
                .setExpiryPolicyFactory(expiryPolicy);

        return cachingProvider.getCacheManager().createCache("ad-request-cache", configuration);
    }
}
