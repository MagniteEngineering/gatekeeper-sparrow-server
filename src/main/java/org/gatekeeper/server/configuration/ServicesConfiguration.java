package org.gatekeeper.server.configuration;

import org.gatekeeper.server.ad.AdRequestService;
import org.gatekeeper.server.dsp.DspBiddingsService;
import org.gatekeeper.server.handler.ad.model.AdRequest;
import org.gatekeeper.server.ssp.SspRulesService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.Cache;

@Configuration
public class ServicesConfiguration {

    @Bean
    DspBiddingsService dspBiddingsService() {
        return new DspBiddingsService();
    }

    @Bean
    SspRulesService sspRulesService() {
        return new SspRulesService();
    }

    @Bean
    AdRequestService adRequestService(Cache<String, AdRequest> adRequestCache) {
        return new AdRequestService(adRequestCache);
    }
}
