package org.gatekeeper.server.handler.dsp;

import io.netty.handler.codec.http.HttpHeaderValues;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import org.apache.commons.lang3.StringUtils;
import org.gatekeeper.server.handler.dsp.model.*;
import org.gatekeeper.server.handler.model.Response;
import org.gatekeeper.server.handler.utils.HttpUtils;
import org.gatekeeper.server.handler.utils.RequestContextUtils;
import org.gatekeeper.server.json.JacksonMapper;

import java.text.MessageFormat;
import java.util.Map;
import java.util.stream.Collectors;

public class BiddingModelValidationHandler implements Handler<RoutingContext> {

    private static final String MESSAGE_PARAM_REQUIRED = "Request parameter \"{0}\" is required";
    private static final String MESSAGE_MODEL_REQUIRED = "Bidding model empty or invalid";

    private JacksonMapper mapper;

    public BiddingModelValidationHandler(JacksonMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void handle(RoutingContext context) {
        String dspId = context.request().getParam(RequestParam.DSP.getCode());
        if (StringUtils.isBlank(dspId)) {
            sendValidationError(context, MessageFormat.format(MESSAGE_PARAM_REQUIRED, RequestParam.DSP.getCode()));
            return;
        }

        BiddingModelRequest request = mapper.decodeValue(context.getBody(), BiddingModelRequest.class);

        if (request.getBiddingModel().isEmpty()) {
            sendValidationError(context, MESSAGE_MODEL_REQUIRED);
            return;
        }

        Map<String, Bidding> biddings = request.getBiddingModel().stream()
                .collect(Collectors.toMap(
                        InterestGroupBidding::getInterest,
                        InterestGroupBidding::getBidding));
        BiddingModelContext model = BiddingModelContext.of(dspId, biddings);
        RequestContextUtils.put(context, BiddingModelContext.class, model);
        context.next();
    }

    private void sendValidationError(RoutingContext event, String error) {
        event.response()
                .putHeader(HttpUtils.CONTENT_TYPE_HEADER, HttpHeaderValues.APPLICATION_JSON)
                .end(mapper.encode(Response.fail(error)));
    }
}
