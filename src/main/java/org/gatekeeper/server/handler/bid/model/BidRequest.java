package org.gatekeeper.server.handler.bid.model;

import lombok.Data;

@Data
public class BidRequest {
    private String id;
    private Impression imp;
}
