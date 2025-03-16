package com.aimatch.common.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    /**
     * JWT密钥
     */
    private String secret = "aimatch-jwt-secret";

    /**
     * token有效期（默认24小时）
     */
    private Long expiration = 86400L;

    /**
     * token头部标识
     */
    private String header = "Authorization";

    /**
     * token前缀
     */
    private String tokenPrefix = "Bearer ";
} 