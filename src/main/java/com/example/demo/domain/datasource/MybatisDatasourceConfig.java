package com.example.demo.domain.datasource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Resource;
import javax.sql.DataSource;

@Configuration
public class MybatisDatasourceConfig {

    @Resource
    private MybatisDataSourceProperties mybatisDataSourceProperties;

    @Bean(name = "mybatisDatasource")
    @ConfigurationProperties(prefix = "spring.datasource.mybatis.hikari")
    public DataSource dataSource() {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(mybatisDataSourceProperties.getUrl());
        ds.setUsername(mybatisDataSourceProperties.getUsername());
        ds.setPassword(mybatisDataSourceProperties.getPassword());
        ds.setDriverClassName(mybatisDataSourceProperties.getDriverClassName());
        ds.setMinimumIdle(1);
        ds.setMaximumPoolSize(1);
        ds.setInitializationFailTimeout(-1);
        return ds;
    }

    @Bean(name = "mybatisJdbcTemplate")
    public JdbcTemplate jdbcTemplate() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource());
        return jdbcTemplate;
    }
}
