package org.gatekeeper.server.handler.ssp.model;

import lombok.Data;

import java.util.List;

@Data
public class Rule {
    private Long zoneId;
    private List<String> dsps;
    private List<String> advertisers;
}
