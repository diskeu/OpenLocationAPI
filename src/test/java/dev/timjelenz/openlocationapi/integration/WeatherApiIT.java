package dev.timjelenz.openlocationapi.integration;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

class WeatherApiIT extends AbstractApiIntegrationTest {

    @Test
    void returnsNotFoundForUnknownLocation() {
        final ResponseEntity<String> response = restTemplate.getForEntity(
            "/api/locations/{id}/weather",
            String.class,
            999_999
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).contains("Location not found");
    }

    @Test
    void rejectsForecastWithoutTimeRange() {
        final ResponseEntity<String> response = restTemplate.getForEntity(
            "/api/locations/{id}/weather/forecast",
            String.class,
            999_999
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void rejectsInvertedForecastTimeRange() {
        final String url = UriComponentsBuilder
            .fromPath("/api/locations/{id}/weather/forecast")
            .queryParam("from", "2024-01-02T00:00:00Z")
            .queryParam("to", "2024-01-01T00:00:00Z")
            .buildAndExpand(999_999)
            .toUriString();

        final ResponseEntity<String> response = restTemplate.getForEntity(
            url,
            String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
