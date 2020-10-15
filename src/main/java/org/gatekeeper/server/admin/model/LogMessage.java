package org.gatekeeper.server.admin.model;

import lombok.Data;

@Data
public class LogMessage {
    private String message;

    public LogMessage(String message) {
        this.message = message;
    }
}
