package com.aimatch.business;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import springfox.documentation.oas.annotations.EnableOpenApi;

/**
 * 业务服务启动类
 *
 * @author AI Match Team
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableOpenApi
@MapperScan("com.aimatch.business.mapper")
public class BusinessApplication {
    public static void main(String[] args) {
        SpringApplication.run(BusinessApplication.class, args);
    }
} 