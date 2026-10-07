package dev.timjelenz.openlocationapi.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import dev.timjelenz.openlocationapi.dto.responses.weather.CurrentWeatherResponse;
import dev.timjelenz.openlocationapi.dto.responses.weather.ForecastResponse;
import dev.timjelenz.openlocationapi.models.Location;

/**
 * Tests against the real WeatherAPI. Disabled unless
 * RUN_LIVE_WEATHER_TESTS=true is set.
 */
@Tag("live")
@EnabledIfEnvironmentVariable(
    named = "RUN_LIVE_WEATHER_TESTS",
    matches = "true"
)
class WeatherApiLiveIT extends AbstractApiIntegrationTest {

    @Test
    void returnsCurrentWeatherFromWeatherApi() {
        final Location berlin = createLocation(
            "Berlin",
            "Europe/Berlin",
            "52.520",
            "13.405"
        );

        final ResponseEntity<CurrentWeatherResponse> response =
            restTemplate.getForEntity(
                "/api/locations/{id}/weather",
                CurrentWeatherResponse.class,
                berlin.getId()
            );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().location().name()).isNotBlank();
        assertThat(response.getBody().location().localTime()).isNotNull();
        assertThat(response.getBody().current().temperatureC()).isNotNull();
        assertThat(response.getBody().current().humidity())
            .isBetween((short) 0, (short) 100);
    }

    @Test
    void returnsForecastFromWeatherApi() {
        final Location berlin = createLocation(
            "Berlin",
            "Europe/Berlin",
            "52.520",
            "13.405"
        );
        final ZonedDateTime from = ZonedDateTime.now(ZoneOffset.UTC)
            .withMinute(0)
            .withSecond(0)
            .withNano(0);
        final ZonedDateTime to = from.plusDays(3);
        final String url = UriComponentsBuilder
            .fromPath("/api/locations/{id}/weather/forecast")
            .queryParam("from", from)
            .queryParam("to", to)
            .buildAndExpand(berlin.getId())
            .toUriString();

        final ResponseEntity<ForecastResponse> response =
            restTemplate.getForEntity(url, ForecastResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().location().name()).isNotBlank();
        assertThat(response.getBody().forecast()).isNotEmpty();
        assertThat(response.getBody().forecast())
            .allSatisfy(day -> assertThat(day.date()).isNotNull());
    }
}
