package com.qinxi1992.im.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;
import org.springframework.web.socket.handler.WebSocketHandlerDecoratorFactory;

@Configuration
@EnableWebSocketMessageBroker
@Slf4j
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry stompEndpointRegistry) {
        stompEndpointRegistry.addEndpoint("/ws").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry messageBrokerRegistry) {
        messageBrokerRegistry.setApplicationDestinationPrefixes("/app");
        //messageBrokerRegistry.enableSimpleBroker("/topic", "/queue");
        messageBrokerRegistry.enableStompBrokerRelay("/topic", "/queue")
                .setSystemLogin("root").setSystemPasscode("root")
                .setClientLogin("root").setClientPasscode("root")
                .setUserDestinationBroadcast("/topic/unresolved-user-destination")
                .setUserRegistryBroadcast("/topic/simp-user-registry")
        ;
    }

    @Override
    public void configureWebSocketTransport(final WebSocketTransportRegistration registration) {
        registration.addDecoratorFactory(new WebSocketHandlerDecoratorFactory() {
            @Override
            public WebSocketHandler decorate(final WebSocketHandler handler) {
                return new WebSocketHandlerDecorator(handler) {
                    @Override
                    public void afterConnectionEstablished(final WebSocketSession session) throws Exception {
                        String username = session.getPrincipal().getName();
                        log.info("online: " + username);
                        super.afterConnectionEstablished(session);
                    }

                    @Override
                    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus)
                            throws Exception {
                        String username = session.getPrincipal().getName();
                        log.info("offline: " + username);
                        super.afterConnectionClosed(session, closeStatus);
                    }
                };
            }
        });
        super.configureWebSocketTransport(registration);
    }
}
