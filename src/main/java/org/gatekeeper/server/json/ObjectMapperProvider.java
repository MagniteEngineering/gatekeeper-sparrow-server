package org.gatekeeper.server.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.json.jackson.DatabindCodec;

public class ObjectMapperProvider {
    private static final ObjectMapper MAPPER;

    static {
        MAPPER = DatabindCodec.mapper().configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
                .enable(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    private ObjectMapperProvider() {
    }

    public static ObjectMapper mapper() {
        return MAPPER;
    }
}
