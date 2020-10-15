package org.gatekeeper.server.configuration;

import org.gatekeeper.server.admin.service.WebSocketBroker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.Optional;

@Configuration
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

    @Bean
    WebSocketBroker webSocketBroker(Optional<SimpMessagingTemplate> simpMessagingTemplate) {
        return new WebSocketBroker(simpMessagingTemplate.orElse(null));
    }

    @Profile("debug")
    @Configuration
    @EnableWebSocketMessageBroker
    static class SimpleWebSocketMessageBroker implements WebSocketMessageBrokerConfigurer {
        @Override
        public void configureMessageBroker(MessageBrokerRegistry config) {
            config.enableSimpleBroker("/topic");
            config.setApplicationDestinationPrefixes("/app");
        }

        @Override
        public void registerStompEndpoints(StompEndpointRegistry registry) {
            registry.addEndpoint("/admin/admin-websocket").withSockJS();
        }
    }
}
