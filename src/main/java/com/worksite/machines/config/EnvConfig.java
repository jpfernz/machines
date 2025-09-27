package com.worksite.machines.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class EnvConfig {

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer(
            ConfigurableEnvironment env) {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

        Map<String, Object> properties = new HashMap<>();
        properties.put("db.url", dotenv.get("DB_URL"));
        properties.put("db.username", dotenv.get("DB_USERNAME"));
        properties.put("db.password", dotenv.get("DB_PASSWORD"));
        properties.put("jwk.set.uri", dotenv.get("JWK_SET_URI"));
        properties.put("client.id", dotenv.get("CLIENT_ID"));
        properties.put("client.secret", dotenv.get("CLIENT_SECRET"));
        properties.put("grant.type", dotenv.get("GRANT_TYPE"));
        properties.put("token.uri", dotenv.get("TOKEN_URI"));

        MapPropertySource propertySource = new MapPropertySource("dotenvProperties", properties);
        env.getPropertySources().addFirst(propertySource);

        return new PropertySourcesPlaceholderConfigurer();
    }
}

