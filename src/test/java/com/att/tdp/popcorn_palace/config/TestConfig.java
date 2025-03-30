package com.att.tdp.popcorn_palace.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.TestPropertySource;

@TestConfiguration
@ComponentScan(
    basePackages = "com.att.tdp.popcorn_palace",
    excludeFilters = {
        @ComponentScan.Filter(
            type = FilterType.REGEX,
            pattern = "com.att.tdp.popcorn_palace.PopcornPalaceApplication"
        ),
        @ComponentScan.Filter(
            type = FilterType.REGEX,
            pattern = "com.att.tdp.popcorn_palace.config.AppConfig"
        )
    }
)
@EntityScan("com.att.tdp.popcorn_palace.model")
@TestPropertySource(locations = "classpath:application-test.yaml")
public class TestConfig {

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        return mapper;
    }
} 