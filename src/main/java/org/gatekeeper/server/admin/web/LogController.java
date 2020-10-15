package org.gatekeeper.server.admin.web;

import org.gatekeeper.server.admin.model.LogMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class LogController {

    @MessageMapping("/logs")
    @SendTo("/topic/logs")
    public LogMessage logs(String message) {
        return new LogMessage(message);
    }

}
