package org.gatekeeper.server.handler.dsp;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import org.gatekeeper.server.dsp.DspBiddingsService;
import org.gatekeeper.server.handler.utils.RequestContextUtils;

@Slf4j
public class BiddingModelHandler implements Handler<RoutingContext> {

    private DspBiddingsService dspBiddingsServices;

    public BiddingModelHandler(DspBiddingsService dspBiddingsServices) {
        this.dspBiddingsServices = dspBiddingsServices;
    }

    @Override
    public void handle(RoutingContext context) {
        BiddingModelContext request = RequestContextUtils.get(context, BiddingModelContext.class);
        String dspId = request.getDspId();

        log.debug("Processing bidding model for dsp {}", dspId);
        context.next();

        if (request.getInterestBiddings().isEmpty()) {
            dspBiddingsServices.invalidateBiddingModel(dspId);
        } else {
            dspBiddingsServices.updateBiddingModel(dspId, request.getInterestBiddings());
        }
    }
}
