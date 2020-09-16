package org.gatekeeper.server.configuration.properties;

import lombok.Data;

import java.util.concurrent.TimeUnit;

@Data
public class GraphiteProperties {
    private String host;
    private int port;
    private String prefix;
    private int interval = 1;
    private TimeUnit intervalUnit = TimeUnit.SECONDS;
}
