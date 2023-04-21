package com.example.booking.testConfig;

import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

@Configuration
@Profile("integration-test")
public class DbTestConfiguration {


    @Bean(name = "hsqlDatasource")
    public DataSource hsqlDataSource() {
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        EmbeddedDatabase db = builder.setType(EmbeddedDatabaseType.HSQL)
	       .addScript("classpath:schema-hsqldb.sql")
	       .addScript("classpath:data-hsqldb.sql")
	       .build();

        return db;
    }

    @Bean(name = "hsqlJdbcTemplate")
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(hsqlDataSource());
    }

}
