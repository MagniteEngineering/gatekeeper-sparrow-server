package org.gatekeeper.server.handler.dsp;

import lombok.Data;
import org.gatekeeper.server.handler.dsp.model.Bidding;

import java.util.Map;

@Data(staticConstructor = "of")
public class BiddingModelContext {
    private final String dspId;
    private final Map<String, Bidding> interestBiddings;
}
