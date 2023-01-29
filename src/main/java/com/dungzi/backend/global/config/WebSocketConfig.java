package com.dungzi.backend.global.config;

import com.dungzi.backend.global.config.security.interceptor.JwtTokenCookieInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private JwtTokenCookieInterceptor jwtTokenCookieInterceptor;


    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/queue");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-chat").addInterceptors(jwtTokenCookieInterceptor).setAllowedOriginPatterns("*");
        registry.addEndpoint("/ws-chat").addInterceptors(jwtTokenCookieInterceptor).setAllowedOriginPatterns("*")
                .withSockJS();
    }
}
