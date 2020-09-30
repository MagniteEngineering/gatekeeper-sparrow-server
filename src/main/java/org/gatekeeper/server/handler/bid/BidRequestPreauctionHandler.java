package org.gatekeeper.server.handler.bid;

import io.netty.handler.codec.http.HttpHeaderValues;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.gatekeeper.server.handler.ad.model.AdRequest;
import org.gatekeeper.server.handler.bid.model.BidRequestContext;
import org.gatekeeper.server.handler.dsp.model.Bidding;
import org.gatekeeper.server.handler.ssp.model.InventoryRule;
import org.gatekeeper.server.handler.ssp.model.Rule;
import org.gatekeeper.server.handler.utils.HttpUtils;
import org.gatekeeper.server.handler.utils.RequestContextUtils;
import org.gatekeeper.server.json.JacksonMapper;
import org.gatekeeper.server.service.DspBiddingsService;
import org.gatekeeper.server.service.SspRulesService;
import org.gatekeeper.server.service.model.Bid;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class BidRequestPreauctionHandler implements Handler<RoutingContext> {

    private JacksonMapper mapper;
    private DspBiddingsService dspBiddingsService;
    private SspRulesService sspRulesService;

    @Setter
    private int bidsLimit = 5;

    public BidRequestPreauctionHandler(
            JacksonMapper mapper,
            DspBiddingsService dspBiddingsService,
            SspRulesService sspRulesService) {
        this.mapper = mapper;
        this.dspBiddingsService = dspBiddingsService;
        this.sspRulesService = sspRulesService;
    }

    @Override
    public void handle(RoutingContext context) {
        AdRequest adRequest = RequestContextUtils.get(context, AdRequest.class);
        BidRequestContext bidRequest = RequestContextUtils.get(context, BidRequestContext.class);
        log.debug("Preauction");

        List<Bid> result = preauction(adRequest, bidRequest);

        if (result.isEmpty()) {
            log.debug("Filtered bids are empty");
            sendNoContent(context);
        } else {
            okResponse(context, result);
        }
    }

    private List<Bid> preauction(AdRequest adRequest, BidRequestContext bidRequest) {
        String interest = adRequest.getInterestGroup();
        String domain = adRequest.getDomain();
        List<InventoryRule> inventoryRules = sspRulesService.getInventoryRules(bidRequest.getSspId());

        return dspBiddingsService.getBiddingModels().entrySet().stream()
                .filter(entry -> entry.getValue().containsKey(interest))                        // filter by interest
                .filter(entry -> entry.getValue().get(interest).getDomains().contains(domain))  // filter by domain
                .filter(entry -> isSspEligible(entry.getKey(), entry.getValue().get(interest), inventoryRules)) // filter dsp block
                .map(entry -> Bid.builder()
                        .dspId(entry.getKey())
                        .ad(entry.getValue().get(interest).getAd())
                        .price(entry.getValue().get(interest).getPrice())
                        .build())
                .sorted(Comparator.comparing(Bid::getPrice).reversed())
                .limit(bidsLimit)
                .collect(Collectors.toList());

    }

    private boolean isSspEligible(String dspId, Bidding bidding, List<InventoryRule> inventoryRules) {
        // todo rules model not clear
        for (InventoryRule iRule : inventoryRules) {
            Rule rule = iRule.getRule();

            // filter by advertiser / dsp /floor
            if (isRuleByAdvertiser(rule, bidding.getAd().getAdvertiser()) || isRuleByDsp(rule, dspId)) {
                if (Boolean.TRUE.equals(iRule.getBlock())) {
                    return false;
                }
                if (iRule.getFloor() != null && bidding.getPrice().compareTo(iRule.getFloor()) < 0) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean isRuleByAdvertiser(Rule rule, String advertiser) {
        return rule.getAdvertisers() != null && rule.getAdvertisers().contains(advertiser);
    }

    private boolean isRuleByDsp(Rule rule, String dspId) {
        return rule.getDsps() != null && rule.getDsps().contains(dspId);
    }

    private void okResponse(RoutingContext context, List<Bid> result) {
        context.response()
                .putHeader(HttpUtils.CONTENT_TYPE_HEADER, HttpHeaderValues.APPLICATION_JSON)
                .end(mapper.encode(result));
    }

    private void sendNoContent(RoutingContext context) {
        context.response()
                .setStatusCode(204)
                .end();
    }
}
