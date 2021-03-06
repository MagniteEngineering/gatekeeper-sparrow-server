package org.gatekeeper.server.configuration;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;
import lombok.extern.slf4j.Slf4j;
import org.gatekeeper.server.handler.FailureHandler;
import org.gatekeeper.server.handler.NoCacheHandler;
import org.gatekeeper.server.handler.RequestLogHandler;
import org.gatekeeper.server.handler.ResponseHandler;
import org.gatekeeper.server.handler.ad.AdRequestHandler;
import org.gatekeeper.server.handler.bid.BidRequestPreauctionHandler;
import org.gatekeeper.server.handler.bid.BidRequestValidationHandler;
import org.gatekeeper.server.handler.dsp.BiddingModelHandler;
import org.gatekeeper.server.handler.dsp.BiddingModelValidationHandler;
import org.gatekeeper.server.handler.ssp.InventoryRulesHandler;
import org.gatekeeper.server.handler.ssp.InventoryRulesValidationHandler;
import org.gatekeeper.server.json.JacksonMapper;
import org.gatekeeper.server.service.AdRequestService;
import org.gatekeeper.server.service.DspBiddingsService;
import org.gatekeeper.server.service.SspRulesService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Configuration
public class RouteConfiguration {
    private static final String SPARROW_API_V1_PREFIX = "/sparrow/api/v1";

    @SuppressWarnings({"unchecked"})
    @Bean
    Map<String, Handler<RoutingContext>> handlerRegistry(
            ApplicationContext applicationContext) {
        String[] beanNamesForType = applicationContext.getBeanNamesForType(Handler.class);
        return Arrays.stream(beanNamesForType)
                .collect(Collectors.toMap(
                        Function.identity(),
                        bean -> (Handler<RoutingContext>) applicationContext.getBean(bean, Handler.class)));
    }

    @Bean
    Router router(
            Vertx vertx,
            Map<String, Handler<RoutingContext>> handlerRegistry) {
        Router router = Router.router(vertx);

        router.route().handler(BodyHandler.create());

        router.route(SPARROW_API_V1_PREFIX + "/*")
                .handler(handlerRegistry.get("requestLogHandler"));

        router.post(SPARROW_API_V1_PREFIX + "/dsp/bidding-model")
                .handler(handlerRegistry.get("biddingModelValidationHandler"))
                .handler(handlerRegistry.get("biddingModelHandler"))
                .handler(handlerRegistry.get("responseHandler"));

        router.post(SPARROW_API_V1_PREFIX + "/ssp/inventory-rules")
                .handler(handlerRegistry.get("inventoryRulesValidationHandler"))
                .handler(handlerRegistry.get("inventoryRulesHandler"))
                .handler(handlerRegistry.get("responseHandler"));

        router.get(SPARROW_API_V1_PREFIX + "/browser/interest-group-ad-request")
                .handler(handlerRegistry.get("noCacheHandler"))
                .handler(handlerRegistry.get("adRequestHandler"));

        router.post(SPARROW_API_V1_PREFIX + "/ssp/interest-group-bid-request")
                .handler(handlerRegistry.get("bidRequestValidationHandler"))
                .handler(handlerRegistry.get("bidRequestPreauctionHandler"));

        router.get("/webroot/*").handler(handlerRegistry.get("staticHandler"));
        router.get("/").handler(handlerRegistry.get("staticHandler"));

        router.route()
                .failureHandler(handlerRegistry.get("failureHandler"));
        return router;
    }

    @Bean
    BidRequestValidationHandler bidRequestValidationHandler(
            @Value("${request.ssp-bid-request.ad-request-lookup-timeout-millis}") long adRequestLookupMillis,
            JacksonMapper jacksonMapper,
            AdRequestService adRequestService) {
        var handler = new BidRequestValidationHandler(jacksonMapper, adRequestService);
        handler.setAdRequestLookupTimeoutMillis(adRequestLookupMillis);
        return handler;
    }

    @Bean
    BidRequestPreauctionHandler bidRequestPreauctionHandler(
            @Value("${request.ssp-bid-request.bids-limit}") int bidsLimit,
            JacksonMapper jacksonMapper,
            DspBiddingsService dspBiddingsService,
            SspRulesService sspRulesService) {
        var handler = new BidRequestPreauctionHandler(jacksonMapper, dspBiddingsService, sspRulesService);
        handler.setBidsLimit(bidsLimit);
        return handler;
    }

    @Bean
    AdRequestHandler adRequestHandler(AdRequestService adRequestService) {
        return new AdRequestHandler(adRequestService);
    }

    @Bean
    InventoryRulesValidationHandler inventoryRulesValidationHandler(JacksonMapper jacksonMapper) {
        return new InventoryRulesValidationHandler(jacksonMapper);
    }

    @Bean
    InventoryRulesHandler inventoryRulesHandler(SspRulesService sspRulesService) {
        return new InventoryRulesHandler(sspRulesService);
    }

    @Bean
    BiddingModelValidationHandler biddingModelValidationHandler(JacksonMapper jacksonMapper) {
        return new BiddingModelValidationHandler(jacksonMapper);
    }

    @Bean
    BiddingModelHandler biddingModelHandler(DspBiddingsService dspBiddingsService) {
        return new BiddingModelHandler(dspBiddingsService);
    }

    @Bean
    ResponseHandler responseHandler(JacksonMapper jacksonMapper) {
        return new ResponseHandler(jacksonMapper);
    }

    @Bean
    StaticHandler staticHandler() {
        return StaticHandler.create();
    }

    @Bean
    FailureHandler failureHandler(JacksonMapper jacksonMapper) {
        return new FailureHandler(jacksonMapper);
    }

    @Bean
    RequestLogHandler requestLogHandler() {
        return new RequestLogHandler();
    }

    @Bean
    NoCacheHandler noCacheHandler() {
        return new NoCacheHandler();
    }
}
