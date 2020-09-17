package org.gatekeeper.server.handler.ssp.model;

import lombok.Getter;

@Getter
public enum RequestParam {
    SSP("sspId");
    private String code;

    RequestParam(String code) {
        this.code = code;
    }
}
