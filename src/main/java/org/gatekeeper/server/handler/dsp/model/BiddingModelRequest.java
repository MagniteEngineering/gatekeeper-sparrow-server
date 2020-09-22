package org.gatekeeper.server.handler.dsp.model;

import lombok.Data;

import java.util.List;

@Data
public class BiddingModelRequest {
    private List<InterestGroupBidding> biddingModel;
}
