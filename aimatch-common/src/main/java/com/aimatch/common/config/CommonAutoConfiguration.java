package com.aimatch.common.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@AutoConfiguration
@ComponentScan(basePackages = "com.aimatch.common.jwt")
public class CommonAutoConfiguration {
} 