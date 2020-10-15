package org.gatekeeper.server.admin.service;

import lombok.extern.slf4j.Slf4j;
import org.gatekeeper.server.admin.model.LogMessage;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Slf4j
public class WebSocketBroker {

    private SimpMessagingTemplate webSocket;

    public WebSocketBroker(SimpMessagingTemplate webSocket) {
        this.webSocket = webSocket;
    }

    public void sendLogMessage(String message) {
        if (webSocket == null) {
            return;
        }
        LogMessage log = new LogMessage(message);
        webSocket.convertAndSend("/topic/logs", log);
    }
}
