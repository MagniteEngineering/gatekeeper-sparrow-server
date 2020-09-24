package org.gatekeeper.server.handler.bid.model;

import lombok.Data;

@Data(staticConstructor = "of")
public class BidRequestContext {
    private final String sspId;
    private final BidRequest bidRequest;
}
