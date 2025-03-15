package com.aimatch.uaa;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import springfox.documentation.oas.annotations.EnableOpenApi;

/**
 * UAA服务启动类
 *
 * @author AI Match Team
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableOpenApi
@MapperScan("com.aimatch.uaa.mapper")
public class UaaApplication {
    public static void main(String[] args) {
        SpringApplication.run(UaaApplication.class, args);
    }
} 