package com.aimatch.uaa.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {
    /**
     * JWT密钥
     */
    private String secret = "aimatch-secret-key";
    
    /**
     * JWT过期时间（小时）
     */
    private Long expiration = 24L;
    
    /**
     * JWT token前缀
     */
    private String tokenPrefix = "Bearer ";
} 