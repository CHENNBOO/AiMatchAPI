package com.aimatch.gateway.filter;

import com.aimatch.common.jwt.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Component
public class AuthenticationFilter implements GlobalFilter, Ordered {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationFilter.class);

    @Autowired
    private JwtUtils jwtUtils;

    private static final List<String> WHITELIST = Arrays.asList(
            "/uaa/api/users/register",
            "/uaa/api/users/login"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        log.info("Processing request path: {}", path);

        // 打印所有请求头
        request.getHeaders().forEach((key, value) -> {
            log.info("Header '{}' = {}", key, value);
        });

        // 如果是白名单中的路径，直接放行
        if (WHITELIST.stream().anyMatch(path::startsWith)) {
            log.info("Path {} is in whitelist, allowing request", path);
            return chain.filter(exchange);
        }

        // 获取token
        String token = extractToken(request);
        if (token == null) {
            log.warn("No token found in request headers");
            return unauthorized(exchange, "No token provided");
        }

        log.info("Found token: {}", token.substring(0, Math.min(token.length(), 20)) + "...");
        if (jwtUtils.validateToken(token) && !jwtUtils.isTokenExpired(token)) {
            log.info("Token is valid, proceeding with request");
            
            // 创建新的请求，将token传递给下游服务
            ServerHttpRequest newRequest = request.mutate()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .build();
            
            // 使用新的请求创建新的exchange
            ServerWebExchange newExchange = exchange.mutate()
                .request(newRequest)
                .build();
            
            return chain.filter(newExchange);
        }

        log.warn("Token validation failed");
        return unauthorized(exchange, "Invalid token");
    }

    private String extractToken(ServerHttpRequest request) {
        String bearerToken = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        log.info("Authorization header: {}", bearerToken);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange, String message) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        log.warn("Unauthorized access: {}", message);
        return response.setComplete();
    }

    @Override
    public int getOrder() {
        return -100;
    }
} 