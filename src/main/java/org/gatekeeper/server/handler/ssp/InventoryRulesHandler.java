package org.gatekeeper.server.handler.ssp;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import org.gatekeeper.server.handler.ssp.model.InventoryRulesContext;
import org.gatekeeper.server.handler.utils.RequestContextUtils;
import org.gatekeeper.server.service.SspRulesService;

@Slf4j
public class InventoryRulesHandler implements Handler<RoutingContext> {

    private SspRulesService sspRulesService;

    public InventoryRulesHandler(SspRulesService sspRulesService) {
        this.sspRulesService = sspRulesService;
    }

    @Override
    public void handle(RoutingContext context) {
        InventoryRulesContext request = RequestContextUtils.get(context, InventoryRulesContext.class);
        String sspId = request.getSspId();

        log.debug("Processing inventory rules for ssp {}", sspId);
        context.next();

        if (request.getInventoryRules().isEmpty()) {
            sspRulesService.invalidateInventoryRules(sspId);
        } else {
            sspRulesService.updateInventoryRules(sspId, request.getInventoryRules());
        }
    }
}
