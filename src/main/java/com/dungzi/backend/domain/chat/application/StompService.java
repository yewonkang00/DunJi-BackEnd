package com.dungzi.backend.domain.chat.application;

import com.dungzi.backend.domain.user.domain.User;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class StompService {

    public User getUserFromHeader(Message message) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        UsernamePasswordAuthenticationToken simpleToken = (UsernamePasswordAuthenticationToken) accessor.getHeader(
                "simpUser");
        return (User) simpleToken.getPrincipal();
    }
}
