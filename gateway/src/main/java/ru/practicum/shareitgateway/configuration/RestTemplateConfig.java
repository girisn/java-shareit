package ru.practicum.shareitgateway.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
public class RestTemplateConfig {
    @Bean
    @Profile("booking")
    public RestTemplate baseBookingsRestTemplate(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        return createRestTemplate(serverUrl + "/bookings", builder);
    }

    @Bean
    @Profile("items")
    public RestTemplate baseItemsRestTemplate(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        return createRestTemplate(serverUrl + "/items", builder);
    }

    @Bean
    @Profile("requests")
    public RestTemplate baseRequestsRestTemplate(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        return createRestTemplate(serverUrl + "/requests", builder);
    }

    @Bean
    @Profile("users")
    public RestTemplate baseUsersRestTemplate(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        return createRestTemplate(serverUrl + "/users", builder);
    }

    private RestTemplate createRestTemplate(String url, RestTemplateBuilder builder) {
        return builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(url))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
    }
}
