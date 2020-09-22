package org.gatekeeper.server.ssp;

import org.gatekeeper.server.handler.ssp.model.InventoryRule;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SspRulesService {

    // todo replace with caching, think through
    private final Map<String, List<InventoryRule>> cache = new ConcurrentHashMap<>();

    public void invalidateInventoryRules(String sspId) {
        cache.remove(sspId);
    }

    public void updateInventoryRules(String sspId, List<InventoryRule> inventoryRules) {
        cache.put(sspId, inventoryRules);
    }

    public List<InventoryRule> getInventoryRules(String sspId) {
        return cache.get(sspId);
    }
}
