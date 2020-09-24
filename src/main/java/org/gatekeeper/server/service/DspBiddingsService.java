package org.gatekeeper.server.service;

import org.gatekeeper.server.handler.dsp.model.Bidding;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DspBiddingsService {

    // todo replace with caching, think through
    private final Map<String, Map<String, Bidding>> cache = new ConcurrentHashMap<>();

    public void updateBiddingModel(String dspId, Map<String, Bidding> model) {
        cache.put(dspId, model);
    }

    public void invalidateBiddingModel(String dspId) {
        cache.remove(dspId);
    }

    public Map<String, Map<String, Bidding>> getBiddingModels() {
        return new HashMap<>(cache);
    }

}
