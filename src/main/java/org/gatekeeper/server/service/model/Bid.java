package org.gatekeeper.server.service.model;

import lombok.Builder;
import lombok.Data;
import org.gatekeeper.server.handler.dsp.model.Ad;

import java.math.BigDecimal;

@Data
@Builder
public class Bid {
    private final String dspId;
    private final BigDecimal price;
    private final Ad ad;
}
