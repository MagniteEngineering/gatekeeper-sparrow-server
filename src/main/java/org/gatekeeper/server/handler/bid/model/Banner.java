package org.gatekeeper.server.handler.bid.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Banner {
    @JsonProperty("w")
    private int width;
    @JsonProperty("h")
    private int height;
    private int pos;
    private int topframe;
}
