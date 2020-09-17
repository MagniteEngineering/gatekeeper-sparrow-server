package org.gatekeeper.server.handler.dsp.model;

import lombok.Data;

import java.util.Map;

@Data(staticConstructor = "of")
public class BiddingModelContext {
    private final String dspId;
    private final Map<String, Bidding> interestBiddings;
}
