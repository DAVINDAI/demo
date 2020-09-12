package com.example.demo.domain.datasource;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Resource;
import javax.sql.DataSource;

@Configuration
public class TestDatasourceConfig {

    @Resource
    private TestDataSourceProperties testDataSourceProperties;

    @Bean(name = "testDatasource")
    @ConfigurationProperties(prefix = "spring.datasource.test.hikari")
    @Primary
    public DataSource dataSource() {
        HikariDataSource ds = testDataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
        return ds;
    }

    @Bean(name = "testJdbcTemplate")
    public JdbcTemplate jdbcTemplate() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource());
        return jdbcTemplate;
    }
}
