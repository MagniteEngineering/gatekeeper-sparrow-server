package org.gatekeeper.server.handler.ssp.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class InventoryRule {
    private Rule rule;
    private Boolean block;
    private BigDecimal floor;
}
