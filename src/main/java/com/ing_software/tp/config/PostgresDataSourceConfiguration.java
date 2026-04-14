package com.ing_software.tp.config;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;

import com.zaxxer.hikari.HikariDataSource;

/**
 * Evita que {@code spring.datasource.url} resuelva mal (p. ej. {@code DATABASE_URL} en
 * {@code postgresql://}, placeholders vacíos, o variables que Render inyecta aparte).
 * En tests, {@code app.postgres-datasource.enabled=false} para usar H2.
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name = "app.postgres-datasource.enabled", havingValue = "true", matchIfMissing = true)
@AutoConfigureBefore(DataSourceAutoConfiguration.class)
@Order(org.springframework.core.Ordered.HIGHEST_PRECEDENCE)
public class PostgresDataSourceConfiguration {

    @Bean
    @Primary
    public DataSource dataSource(Environment environment) {
        String raw = resolveRawUrl(environment);
        if (raw == null || raw.isBlank()) {
            throw new IllegalStateException(
                    "Definí POSTGRES_DOCKER_URL o DATABASE_URL (Render suele inyectar DATABASE_URL).");
        }
        String trimmed = PostgresUrlEnvironmentPostProcessor.stripQuotes(raw.trim());
        String jdbcUrl = PostgresUrlEnvironmentPostProcessor.toJdbcUrl(trimmed);
        if (!jdbcUrl.startsWith("jdbc:")) {
            throw new IllegalStateException("URL de base inválida (se esperaba jdbc: o postgresql:): " + jdbcUrl);
        }
        String username = firstNonBlank(
                environment.getProperty("POSTGRES_USERNAME"),
                environment.getProperty("spring.datasource.username"));
        String password = firstNonBlank(
                environment.getProperty("POSTGRES_PASSWORD"),
                environment.getProperty("spring.datasource.password"));
        HikariDataSource ds = DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .url(jdbcUrl)
                .username(username != null ? username : "")
                .password(password != null ? password : "")
                .build();
        ds.setAutoCommit(false);
        return ds;
    }

    private static String resolveRawUrl(Environment env) {
        for (String key : new String[] {
                "POSTGRES_DOCKER_URL",
                "DATABASE_URL",
                "SPRING_DATASOURCE_URL",
        }) {
            String v = PostgresUrlEnvironmentPostProcessor.stripQuotes(env.getProperty(key));
            if (v != null && !v.isBlank()) {
                return v;
            }
        }
        String fromSpring = PostgresUrlEnvironmentPostProcessor.stripQuotes(env.getProperty("spring.datasource.url"));
        if (fromSpring != null
                && !fromSpring.isBlank()
                && !fromSpring.startsWith("${")) {
            return fromSpring;
        }
        return null;
    }

    private static String firstNonBlank(String a, String b) {
        if (a != null && !a.isBlank()) {
            return a;
        }
        if (b != null && !b.isBlank()) {
            return b;
        }
        return null;
    }
}
