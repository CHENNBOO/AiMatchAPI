package com.aimatch.gateway.filter;

import com.aimatch.common.jwt.JwtPayload;
import com.aimatch.common.jwt.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
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
        
        log.info("============ 请求处理开始 ============");
        log.info("请求详细信息:");
        log.info("请求路径: {}", path);
        log.info("完整URI: {}", request.getURI());
        log.info("请求方法: {}", request.getMethod());
        log.info("远程地址: {}", request.getRemoteAddress());

        // 打印所有请求头
        log.info("============ 请求头信息 ============");
        request.getHeaders().forEach((key, value) -> {
            log.info("Header '{}' = {}", key, value);
        });

        // 打印查询参数
        log.info("============ 查询参数 ============");
        request.getQueryParams().forEach((key, value) -> {
            log.info("Query '{}' = {}", key, value);
        });

        // 如果是白名单中的路径，直接放行
        if (WHITELIST.stream().anyMatch(path::startsWith)) {
            log.info("============ 白名单请求 ============");
            log.info("路径 {} 在白名单中，直接放行", path);
            log.info("白名单列表: {}", WHITELIST);
            return chain.filter(exchange);
        }

        // 获取token
        String token = extractToken(request);
        if (token == null) {
            log.warn("============ Token缺失 ============");
            log.warn("请求头中未找到token或token格式不正确");
            return unauthorized(exchange, "未提供token");
        }

        log.info("============ Token信息 ============");
        log.info("获取到token: {}", token.substring(0, Math.min(token.length(), 20)) + "...");
        
        try {
            log.info("============ Token验证开始 ============");
            boolean isValid = jwtUtils.validateToken(token);
            log.info("Token验证结果: {}", isValid ? "有效" : "无效");
            
            if (!isValid) {
                log.warn("Token验证失败，可能的原因：签名无效或token格式错误");
                return unauthorized(exchange, "无效的token");
            }
            
            boolean isExpired = jwtUtils.isTokenExpired(token);
            log.info("Token是否过期: {}", isExpired);
            
            if (isExpired) {
                log.warn("Token已过期");
                return unauthorized(exchange, "token已过期");
            }
            
            // 如果token验证通过，打印token中的信息
            try {
                log.info("============ Token负载信息 ============");
                JwtPayload payload = jwtUtils.getPayloadFromToken(token);
                log.info("Token解析成功:");
                log.info("- 用户ID: {}", payload.getUserId());
                log.info("- 用户名: {}", payload.getUsername());
                log.info("- 角色: {}", Arrays.toString(payload.getRoles()));
            } catch (Exception e) {
                log.warn("Token payload解析失败: {}", e.getMessage());
            }
            
            // 创建新的请求，将token传递给下游服务
            log.info("============ 请求转发准备 ============");
            ServerHttpRequest newRequest = request.mutate()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .build();
            
            // 打印转发前的请求信息
            log.info("转发前的请求信息:");
            log.info("- 目标路径: {}", newRequest.getPath());
            log.info("- 目标URI: {}", newRequest.getURI());
            log.info("- 请求方法: {}", newRequest.getMethod());
            log.info("- 请求头:");
            newRequest.getHeaders().forEach((key, value) -> {
                log.info("  {} = {}", key, value);
            });
            
            // 使用新的请求创建新的exchange
            ServerWebExchange newExchange = exchange.mutate()
                .request(newRequest)
                .build();
            
            log.info("============ 开始转发请求 ============");
            return chain.filter(newExchange)
                .doOnSuccess(v -> log.info("============ 请求转发成功 ============"))
                .doOnError(error -> log.error("============ 请求转发失败 ============\n错误信息: {}", error.getMessage()));
        } catch (Exception e) {
            log.error("============ Token处理异常 ============");
            log.error("异常类型: {}", e.getClass().getName());
            log.error("异常信息: {}", e.getMessage());
            log.error("异常堆栈:", e);
            return unauthorized(exchange, "Token处理失败");
        }
    }

    private String extractToken(ServerHttpRequest request) {
        log.info("============ 提取Token开始 ============");
        String bearerToken = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        log.info("Authorization请求头: {}", bearerToken);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7);
            log.info("成功提取token，长度: {}", token.length());
            return token;
        }
        log.warn("Authorization请求头格式不正确或不存在");
        return null;
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange, String message) {
        log.warn("============ 未授权访问 ============");
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8");
        
        String body = String.format("{\"code\":401,\"message\":\"%s\"}", message);
        DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
        
        log.warn("未授权原因: {}", message);
        return response.writeWith(Mono.just(buffer));
    }

    @Override
    public int getOrder() {
        return -100;
    }
} 