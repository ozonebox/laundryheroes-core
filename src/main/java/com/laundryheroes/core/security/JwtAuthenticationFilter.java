package com.laundryheroes.core.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.laundryheroes.core.auth.JwtService;
import com.laundryheroes.core.user.User;
import com.laundryheroes.core.user.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String token = extractJwtFromCookie(request);

        if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {

                String email = jwtService.extractEmail(token);

                userRepository.findByEmail(email).ifPresent(user -> {

                    // Read authLevel from JWT (FULL or PENDING)
                    String authLevel = jwtService.extractAuthLevel(token); // NEW METHOD YOU WILL ADD

                    boolean valid = jwtService.isTokenValid(token, email, user.getProfileStatus().name());

                    if (valid) {
                        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

                        // 👇 Add user ROLE (CUSTOMER / ADMIN / DRIVER / etc)
                        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));

                        // 👇 Add AUTH level (AUTH_FULL or AUTH_PENDING)
                        authorities.add(new SimpleGrantedAuthority("AUTH_" + authLevel));

                        UsernamePasswordAuthenticationToken auth =
                                new UsernamePasswordAuthenticationToken(
                                        user,
                                        null,
                                        authorities
                                );

                        auth.setDetails(
                                new WebAuthenticationDetailsSource().buildDetails(request)
                        );

                        SecurityContextHolder.getContext().setAuthentication(auth);
                    }
                });

            } catch (Exception ignored) {
            }
        }

        filterChain.doFilter(request, response);
    }

    private String extractJwtFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;

        for (Cookie cookie : cookies) {
            if ("AUTH_TOKEN".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
