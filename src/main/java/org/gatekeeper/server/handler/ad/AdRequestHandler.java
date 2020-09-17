package org.gatekeeper.server.handler.ad;

import io.vertx.core.Handler;
import io.vertx.core.MultiMap;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.gatekeeper.server.ad.AdRequestService;
import org.gatekeeper.server.handler.ad.model.AdRequest;
import org.gatekeeper.server.handler.ad.model.RequestParam;
import org.gatekeeper.server.handler.utils.HttpUtils;

@Slf4j
public class AdRequestHandler implements Handler<RoutingContext> {

    private AdRequestService adRequestService;

    public AdRequestHandler(AdRequestService adRequestService) {
        this.adRequestService = adRequestService;
    }

    @Override
    public void handle(RoutingContext context) {
        MultiMap params = context.queryParams();
        String origin = context.request().getHeader(HttpUtils.ORIGIN_HEADER);

        var ad = AdRequest.builder()
                .domain(origin)
                .impressionId(params.get(RequestParam.IMPRESSION_ID.getCode()))
                .interestGroup(params.get(RequestParam.INTEREST_GROUP.getCode()))
                .cohort(params.get(RequestParam.COHORT.getCode()))
                .inventoryId(params.get(RequestParam.INVENTORY_ID.getCode()))
                .contextual(params.get(RequestParam.CONTEXTUAL.getCode()))
                .build();

        if (!isValid(ad)) {
            context.response()
                    .putHeader(HttpUtils.CACHE_CONTROL_HEADER, "no-cache")
                    .setStatusCode(400)
                    .end();
            return;
        }

        adRequestService.cacheAdRequest(ad.getImpressionId(), ad);

        log.debug("Cache: {}={}", ad.getImpressionId(), adRequestService.getAdRequest(ad.getImpressionId()));
        context.response()
                .putHeader(HttpUtils.CACHE_CONTROL_HEADER, "no-cache")
                .end();
    }

    private boolean isValid(AdRequest ad) {
        if (StringUtils.isBlank(ad.getDomain())) {
            return false;
        }
        if (StringUtils.isBlank(ad.getImpressionId())) {
            return false;
        }
        return true;
    }
}
