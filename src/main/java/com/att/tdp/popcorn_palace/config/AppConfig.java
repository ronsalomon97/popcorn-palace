package com.att.tdp.popcorn_palace.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {
    "com.att.tdp.popcorn_palace.controller",
    "com.att.tdp.popcorn_palace.service",
    "com.att.tdp.popcorn_palace.mapper"
})
public class AppConfig {
} 