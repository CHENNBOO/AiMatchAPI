package com.aimatch.common.jwt;

import com.aimatch.common.jwt.JwtPayload;
import com.aimatch.common.jwt.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtUtils {

    private final JwtProperties jwtProperties;

    /**
     * 生成JWT token
     */
    public String generateToken(JwtPayload payload) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", payload.getUserId());
        claims.put("username", payload.getUsername());
        claims.put("roles", payload.getRoles());
        if (payload.getAdditionalInfo() != null) {
            claims.putAll(payload.getAdditionalInfo());
        }

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getExpiration() * 1000))
                .signWith(SignatureAlgorithm.HS512, jwtProperties.getSecret())
                .compact();
    }

    /**
     * 从token中获取JWT中的负载
     */
    public Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtProperties.getSecret())
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 从token中获取JwtPayload
     */
    public JwtPayload getPayloadFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return JwtPayload.builder()
                .userId(claims.get("userId", String.class))
                .username(claims.get("username", String.class))
                .roles(claims.get("roles", String[].class))
                .build();
    }

    /**
     * 验证token是否有效
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(jwtProperties.getSecret())
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断token是否已经过期
     */
    public boolean isTokenExpired(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getExpiration().before(new Date());
    }

    /**
     * 从请求头中获取token字符串
     */
    public String getTokenFromHeader(String header) {
        if (header != null && header.startsWith(jwtProperties.getTokenPrefix())) {
            return header.substring(jwtProperties.getTokenPrefix().length());
        }
        return null;
    }
} 