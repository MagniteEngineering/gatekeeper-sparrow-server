package org.gatekeeper.server.handler.ssp;

import io.netty.handler.codec.http.HttpHeaderValues;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import org.apache.commons.lang3.StringUtils;
import org.gatekeeper.server.handler.model.Response;
import org.gatekeeper.server.handler.ssp.model.InventoryRulesContext;
import org.gatekeeper.server.handler.ssp.model.InventoryRulesRequest;
import org.gatekeeper.server.handler.ssp.model.RequestParam;
import org.gatekeeper.server.handler.utils.HttpUtils;
import org.gatekeeper.server.handler.utils.RequestContextUtils;
import org.gatekeeper.server.json.JacksonMapper;

import java.text.MessageFormat;

public class InventoryRulesValidationHandler implements Handler<RoutingContext> {

    private static final String MESSAGE_PARAM_REQUIRED = "Request parameter \"{0}\" is required";
    private static final String MESSAGE_MODEL_REQUIRED = "Inventory rules empty or invalid";

    private JacksonMapper mapper;

    public InventoryRulesValidationHandler(JacksonMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void handle(RoutingContext context) {
        String sspId = context.request().getParam(RequestParam.SSP.getCode());
        if (StringUtils.isBlank(sspId)) {
            sendValidationError(context, MessageFormat.format(MESSAGE_PARAM_REQUIRED, RequestParam.SSP.getCode()));
            return;
        }

        InventoryRulesRequest request = mapper.decodeValue(context.getBody(), InventoryRulesRequest.class);

        if (request == null || request.getInventoryRules() == null || request.getInventoryRules().isEmpty()) {
            sendValidationError(context, MESSAGE_MODEL_REQUIRED);
            return;
        }

        InventoryRulesContext model = InventoryRulesContext.of(sspId, request.getInventoryRules());
        RequestContextUtils.put(context, InventoryRulesContext.class, model);
        context.next();
    }

    private void sendValidationError(RoutingContext event, String error) {
        event.response()
                .putHeader(HttpUtils.CONTENT_TYPE_HEADER, HttpHeaderValues.APPLICATION_JSON)
                .end(mapper.encode(Response.fail(error)));
    }
}
