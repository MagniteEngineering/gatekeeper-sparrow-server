package org.gatekeeper.server.handler.ad.model;

import lombok.Getter;

@Getter
public enum RequestParam {
    IMPRESSION_ID("impressionId"),
    INTEREST_GROUP("interestGroup"),
    COHORT("cohort"),
    INVENTORY_ID("inventoryId"),
    CONTEXTUAL("contextual");

    private String code;

    RequestParam(String code) {
        this.code = code;
    }
}
