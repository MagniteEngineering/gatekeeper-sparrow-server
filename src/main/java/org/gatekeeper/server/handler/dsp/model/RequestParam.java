package org.gatekeeper.server.handler.dsp.model;

import lombok.Getter;

@Getter
public enum RequestParam {
    DSP("dspId");
    private String code;

    RequestParam(String code) {
        this.code = code;
    }
}
