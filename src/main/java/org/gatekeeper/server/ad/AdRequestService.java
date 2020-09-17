package org.gatekeeper.server.ad;

import org.gatekeeper.server.handler.ad.model.AdRequest;

import javax.cache.Cache;

public class AdRequestService {
    private Cache<String, AdRequest> adRequestCache;

    public AdRequestService(Cache<String, AdRequest> adRequestCache) {
        this.adRequestCache = adRequestCache;
    }

    public void cacheAdRequest(String impressionId, AdRequest adRequest) {
        adRequestCache.put(impressionId, adRequest);
    }

    public AdRequest getAdRequest(String impressionId) {
        return adRequestCache.get(impressionId);
    }
}
