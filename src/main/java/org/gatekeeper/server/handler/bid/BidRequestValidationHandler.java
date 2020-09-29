package org.gatekeeper.server.handler.bid;

import io.netty.handler.codec.http.HttpHeaderValues;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.ext.web.RoutingContext;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.gatekeeper.server.handler.ad.model.AdRequest;
import org.gatekeeper.server.handler.bid.model.BidRequest;
import org.gatekeeper.server.handler.bid.model.BidRequestContext;
import org.gatekeeper.server.handler.model.Response;
import org.gatekeeper.server.handler.utils.HttpUtils;
import org.gatekeeper.server.handler.utils.RequestContextUtils;
import org.gatekeeper.server.json.JacksonMapper;
import org.gatekeeper.server.service.AdRequestService;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Slf4j
public class BidRequestValidationHandler implements Handler<RoutingContext> {

    private static final long CACHE_POP_DELAY_MILLIS = 20;
    private static final long CACHE_TIMEOUT_MILLIS = TimeUnit.SECONDS.toMillis(1);

    private static final Random RANDOM = new Random();

    @Setter
    private long adRequestLookupTimeoutMillis = CACHE_TIMEOUT_MILLIS;
    private JacksonMapper mapper;
    private AdRequestService adRequestService;

    public BidRequestValidationHandler(JacksonMapper mapper, AdRequestService adRequestService) {
        this.mapper = mapper;
        this.adRequestService = adRequestService;
    }

    @Override
    public void handle(RoutingContext context) {
        String sspId = context.request().getParam("sspId");
        if (StringUtils.isBlank(sspId)) {
            sendValidationError(context, "Missing ssp id");
            return;
        }

        BidRequest bidRequest = mapper.decodeValue(context.getBody(), BidRequest.class);
        if (isInvalid(bidRequest)) {
            sendValidationError(context, "Missing impression id");
            return;
        }

        Future<AdRequest> adRequestFuture = pollAdRequest(context, bidRequest.getImp().getId());

        adRequestFuture.onComplete(result -> {
            if (result.failed()) {
                log.debug("No corresponding impression id found in cache");
                sendNoContent(context);
                return;
            }

            BidRequestContext bidRequestContext = BidRequestContext.of(sspId, bidRequest);
            RequestContextUtils.put(context, BidRequestContext.class, bidRequestContext);
            RequestContextUtils.put(context, AdRequest.class, result.result());
            context.next();
        });
    }

    private boolean isInvalid(BidRequest bidRequest) {
        return bidRequest == null
                || StringUtils.isBlank(bidRequest.getId())
                || bidRequest.getImp() == null
                || StringUtils.isBlank(bidRequest.getImp().getId());
    }

    private Future<AdRequest> pollAdRequest(RoutingContext context, String impressionId) {
        Promise<AdRequest> promise = Promise.promise();
        AdRequest adRequest = adRequestService.popAdRequest(impressionId);
        if (adRequest != null) {
            promise.complete(adRequest);
        } else {
            StopWatch stopWatch = StopWatch.createStarted();
            schedulePopAdRequest(context, impressionId, promise, stopWatch);
        }
        return promise.future();
    }

    private void schedulePopAdRequest(
            RoutingContext context,
            String impressionId,
            Promise<AdRequest> promise,
            StopWatch stopWatch) {
        context.vertx().setTimer(CACHE_POP_DELAY_MILLIS + RANDOM.nextInt(5), res -> {
            AdRequest adRequest = adRequestService.popAdRequest(impressionId);
            if (adRequest != null) {
                promise.complete(adRequest);
            } else if (stopWatch.getTime() > adRequestLookupTimeoutMillis) {
                promise.fail("No corresponding impression id found in cache");
            } else {
                schedulePopAdRequest(context, impressionId, promise, stopWatch);
            }
        });
    }

    private void sendValidationError(RoutingContext event, String error) {
        event.response()
                .putHeader(HttpUtils.CONTENT_TYPE_HEADER, HttpHeaderValues.APPLICATION_JSON)
                .end(mapper.encode(Response.fail(error)));
    }

    private void sendNoContent(RoutingContext context) {
        context.response()
                .setStatusCode(204)
                .end();
    }
}
