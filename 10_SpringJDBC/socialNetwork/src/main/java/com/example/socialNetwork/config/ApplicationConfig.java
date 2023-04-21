package com.example.socialNetwork.config;

import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

@Slf4j
@Configuration
public class ApplicationConfig {

    @Value("${maxThreadPoolSize}")
    private int maxThreadPoolSize;

    @Bean(name = "pgDataSource")
    public DataSource getDataSource() {
        DataSourceBuilder builder = DataSourceBuilder.create();
        builder.password("root");
        builder.username("postgres");
        builder.url("jdbc:postgresql://localhost:5432/postgres");
        builder.driverClassName("org.postgresql.Driver");

        return builder.build();
    }


    @Bean(name = "hikariDataSource")
    public DataSource getHikariDataSource() {
        log.info(String.format("the count of max thread to work with Db is : %d",
	       maxThreadPoolSize));
        HikariDataSource hikariDataSource = new HikariDataSource();

        hikariDataSource.setMaximumPoolSize(maxThreadPoolSize);
        hikariDataSource.setPassword("root");
        hikariDataSource.setUsername("postgres");
        hikariDataSource.setDriverClassName("org.postgresql.Driver");
        hikariDataSource.setJdbcUrl("jdbc:postgresql://localhost:5432/postgres");

        return hikariDataSource;
    }

    @Bean(name = "simpleJdbcInsert")
    public SimpleJdbcInsert getSimpleJdbcInsert() {
        return new SimpleJdbcInsert(getHikariDataSource());
    }

    @Bean(name = "hikariJdbcTemplate")
    public JdbcTemplate getHikariJdbcTemplate() {
        return new JdbcTemplate(getHikariDataSource());
    }


}
