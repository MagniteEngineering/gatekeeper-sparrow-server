package org.gatekeeper.server.handler.ad.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AdRequest {
    private final String domain;
    private final String impressionId;
    private final String interestGroup;
    private final String cohort;
    private final String inventoryId;
    private final String contextual;
}
