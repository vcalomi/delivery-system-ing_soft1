package com.ing_software.tp.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

/**
 * Render usa {@code postgresql://}; Spring JDBC exige {@code jdbc:postgresql://}.
 * Se ejecuta antes del autoconfig de DataSource (registro en META-INF/spring/...).
 */
public class PostgresUrlEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {

    private static final String SOURCE_NAME = "normalizedPostgresJdbcUrl";

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        String raw = firstNonBlank(
                environment,
                "POSTGRES_DOCKER_URL",
                "DATABASE_URL",
                "SPRING_DATASOURCE_URL");
        if (raw == null) {
            return;
        }
        String jdbcUrl = toJdbcUrl(raw);
        if (!jdbcUrl.startsWith("jdbc:")) {
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("spring.datasource.url", jdbcUrl);
        environment.getPropertySources().addFirst(new MapPropertySource(SOURCE_NAME, map));
    }

    static String stripQuotes(String s) {
        if (s == null) {
            return null;
        }
        String t = s.trim();
        if (t.length() >= 2) {
            char first = t.charAt(0);
            char last = t.charAt(t.length() - 1);
            if ((first == '"' && last == '"') || (first == '\'' && last == '\'')) {
                return t.substring(1, t.length() - 1).trim();
            }
        }
        return t;
    }

    static String toJdbcUrl(String url) {
        if (url.startsWith("jdbc:")) {
            return url;
        }
        if (url.startsWith("postgresql://")) {
            return "jdbc:" + url;
        }
        if (url.startsWith("postgres://")) {
            return "jdbc:postgresql://" + url.substring("postgres://".length());
        }
        return url;
    }

    private static String firstNonBlank(ConfigurableEnvironment environment, String... keys) {
        for (String key : keys) {
            String v = stripQuotes(environment.getProperty(key));
            if (v != null && !v.isBlank()) {
                return v;
            }
        }
        return null;
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
