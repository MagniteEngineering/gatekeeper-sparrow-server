package org.gatekeeper.server.handler.dsp.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

@Data
public class Bidding {
    private BigDecimal price;
    private Ad ad;
    private Set<String> domains;
}
