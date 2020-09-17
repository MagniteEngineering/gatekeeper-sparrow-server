package org.gatekeeper.server.dsp;

import org.gatekeeper.server.handler.dsp.model.Bidding;

import java.util.List;
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

    public List<Bidding> getBiddings(String interest, String domain) {
        // todo implement
        return List.of();
    }

}
