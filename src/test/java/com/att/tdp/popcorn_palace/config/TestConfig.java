package com.att.tdp.popcorn_palace.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.context.TestPropertySource;

import javax.sql.DataSource;

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
@EnableJpaRepositories(
    basePackages = "com.att.tdp.popcorn_palace.repository",
    excludeFilters = @ComponentScan.Filter(
        type = FilterType.REGEX,
        pattern = "com.att.tdp.popcorn_palace.PopcornPalaceApplication"
    )
)
@TestPropertySource(locations = "classpath:application-test.yaml")
public class TestConfig {

    @Primary
    public DataSourceInitializer dataSourceInitializer(DataSource dataSource) {
        ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator();
        resourceDatabasePopulator.addScript(new org.springframework.core.io.ClassPathResource("schema-test.sql"));
        resourceDatabasePopulator.addScript(new org.springframework.core.io.ClassPathResource("data-test.sql"));
        resourceDatabasePopulator.setSeparator(";");

        DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
        dataSourceInitializer.setDataSource(dataSource);
        dataSourceInitializer.setDatabasePopulator(resourceDatabasePopulator);
        return dataSourceInitializer;
    }
} 