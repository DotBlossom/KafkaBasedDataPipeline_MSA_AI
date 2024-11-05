package com.example.kafkaAsyncTest.security;

import com.example.kafkaAsyncTest.common.customKafkaFilterSet.JwtUtils;
import com.example.kafkaAsyncTest.entity.User;
import com.example.kafkaAsyncTest.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        log.info("Request URI: {}", request.getRequestURI());

        String uri = request.getRequestURI();

        // 필터링에서 제외할 경로들
        if (uri.equals("/api/auth/login") || uri.equals("/api/auth/register") || uri.startsWith("/uploads/")) {
            log.info("Skipping JWT filter for {}", uri);
            chain.doFilter(request, response);
            return;
        }

        // JWT 토큰 검증 로직
        final String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String subject = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            subject = jwtUtils.getSubjectFromToken(jwt);
        } else {
            log.error("Authorization 헤더 누락 또는 토큰 형식 오류");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid JWT Token");
            response.getWriter().flush();
            return;
        }

        User user = userRepository.findByUsername(subject);
        if (jwtUtils.validateToken(jwt, user)) {
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            log.info("User authenticated successfully: {}", user.getUsername());
            chain.doFilter(request, response);
        } else {
            log.error("사용자 정보가 일치하지 않습니다.");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid JWT Token");
            response.getWriter().flush();
            return;
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        // "/uploads/" 및 "/ws/chat" 경로에 대해서는 필터링을 하지 않음
        return path.startsWith("/uploads/") || path.startsWith("/ws/chat");
    }
}