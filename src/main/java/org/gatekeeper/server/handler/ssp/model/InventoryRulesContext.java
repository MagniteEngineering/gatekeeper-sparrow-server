package org.gatekeeper.server.handler.ssp.model;

import lombok.Data;

import java.util.List;

@Data(staticConstructor = "of")
public class InventoryRulesContext {
    private final String sspId;
    private final List<InventoryRule> inventoryRules;
}
