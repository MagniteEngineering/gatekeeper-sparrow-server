package org.gatekeeper.server.configuration;

import org.gatekeeper.server.dsp.DspBiddingsService;
import org.gatekeeper.server.ssp.SspRulesService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
}
