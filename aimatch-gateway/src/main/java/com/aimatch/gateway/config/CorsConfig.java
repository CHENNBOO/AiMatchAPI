package com.aimatch.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.util.pattern.PathPatternParser;

import java.util.Arrays;

@Configuration
public class CorsConfig {
    
    @Bean
    public CorsWebFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        // 允许的域名（开发+生产）
        config.addAllowedOrigin("http://localhost:5173");
        config.addAllowedOrigin("http://1.15.33.197");
        config.addAllowedOrigin("http://aiunity.top");
        // 允许的请求头
        config.addAllowedHeader("*");
        // 允许的方法
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // 允许携带认证信息
        config.setAllowCredentials(true);
        // 预检请求的有效期，单位为秒
        config.setMaxAge(6000L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(new PathPatternParser());
        source.registerCorsConfiguration("/**", config);
        
        return new CorsWebFilter(source);
    }
} 