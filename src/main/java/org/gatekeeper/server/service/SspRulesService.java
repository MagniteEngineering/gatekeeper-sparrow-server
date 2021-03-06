package org.gatekeeper.server.service;

import org.gatekeeper.server.handler.ssp.model.InventoryRule;

import java.util.List;
import java.util.Map;
import java.util.Optional;
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
        return Optional.ofNullable(cache.get(sspId)).orElse(List.of());
    }
}
