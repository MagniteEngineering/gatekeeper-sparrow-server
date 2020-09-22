package org.gatekeeper.server.handler.ssp.model;

import lombok.Data;

import java.util.List;

@Data
public class InventoryRulesRequest {
    private List<InventoryRule> inventoryRules;
}
