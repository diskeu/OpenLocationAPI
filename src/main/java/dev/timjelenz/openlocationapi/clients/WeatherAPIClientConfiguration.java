package dev.timjelenz.openlocationapi.clients;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class WeatherAPIClientConfiguration {

    @Bean
    public RestClient weatherAPIRestClient(RestClient.Builder builder) {
        return builder
            .baseUrl("http://api.weatherapi.com/v1/")
            .build();
    }
}
