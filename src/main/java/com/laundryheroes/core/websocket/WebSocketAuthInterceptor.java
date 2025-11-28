package com.laundryheroes.core.websocket;

import com.laundryheroes.core.auth.JwtService;
import com.laundryheroes.core.user.User;
import com.laundryheroes.core.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.*;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor == null) {
            return message;
        }

        // Only intercept CONNECT frame
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {

            String authHeader = accessor.getFirstNativeHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new MessagingException("Missing Authorization header");
            }

            String token = authHeader.substring(7);

            //Extract email from JWT
            String email = jwtService.extractEmail(token);

            // Load user from DB
            Optional<User> optionalUser = userRepository.findByEmail(email);
            if (optionalUser.isEmpty()) {
                throw new MessagingException("User not found");
            }

            User user = optionalUser.get();

            // Optional: validate token integrity vs profile status
            if (!jwtService.isAccessTokenValid(
                    token,
                    user.getEmail(),
                    user.getProfileStatus().name()
            )) {
                throw new MessagingException("Invalid or expired token");
            }

            // Attach authenticated user to WebSocket session
            accessor.setUser(new StompUserPrincipal(user));

            accessor.getSessionAttributes().put("USER", user);
        }

        return message;
    }

    /**
     * Simple Principal wrapper for WebSocket
     */
    private record StompUserPrincipal(User user) implements Principal {
        @Override
        public String getName() {
            return user.getEmail();
        }
    }
}
