package dev.timjelenz.openlocationapi.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import dev.timjelenz.openlocationapi.dto.responses.SliceResponse;
import dev.timjelenz.openlocationapi.dto.responses.snapshot.WeatherSnapshotResponse;
import dev.timjelenz.openlocationapi.models.Location;
import dev.timjelenz.openlocationapi.models.WeatherSnapshot;

class WeatherSnapshotApiIT extends AbstractApiIntegrationTest {

    private static final LocalDateTime BASE_TIME =
        LocalDateTime.of(2024, 1, 1, 12, 0);

    @Test
    void returnsSnapshotById() {
        final Location location = createLocation(
            "Berlin",
            "Europe/Berlin",
            "52.520",
            "13.405"
        );
        final WeatherSnapshot snapshot = createSnapshot(
            location,
            BASE_TIME,
            "12.5",
            "20.0",
            (short) 60,
            (short) 2,
            (short) 1000
        );

        final ResponseEntity<WeatherSnapshotResponse> response =
            restTemplate.getForEntity(
                "/api/snapshots/{id}",
                WeatherSnapshotResponse.class,
                snapshot.getId()
            );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().id()).isEqualTo(snapshot.getId());
        assertThat(response.getBody().location().name()).isEqualTo("Berlin");
        assertThat(response.getBody().snapshotTime()).isEqualTo(BASE_TIME);
        assertThat(response.getBody().localTime())
            .isEqualTo(BASE_TIME.plusHours(1));
        assertThat(response.getBody().temperatureC())
            .isEqualByComparingTo("12.5");
    }

    @Test
    void returnsAllSnapshotsWithoutFilters() {
        final Location location = createLocation(
            "Berlin",
            "Europe/Berlin",
            "52.520",
            "13.405"
        );
        createSnapshot(
            location,
            BASE_TIME,
            "5.0",
            "10.0",
            (short) 40,
            (short) 1,
            (short) 1000
        );
        createSnapshot(
            location,
            BASE_TIME.plusHours(1),
            "15.0",
            "15.0",
            (short) 60,
            (short) 3,
            (short) 1003
        );

        final SliceResponse<WeatherSnapshotResponse> response =
            search("/api/snapshots");

        assertThat(response.content()).hasSize(2);
        assertThat(response.page()).isZero();
        assertThat(response.size()).isEqualTo(20);
        assertThat(response.hasNext()).isFalse();
    }

    @Test
    void filtersSnapshotsByTemperatureTimeAndHumidity() {
        final Location location = createLocation(
            "Berlin",
            "Europe/Berlin",
            "52.520",
            "13.405"
        );
        createSnapshot(
            location,
            BASE_TIME,
            "5.0",
            "10.0",
            (short) 40,
            (short) 1,
            (short) 1000
        );
        createSnapshot(
            location,
            BASE_TIME.plusHours(1),
            "15.0",
            "15.0",
            (short) 60,
            (short) 3,
            (short) 1003
        );
        createSnapshot(
            location,
            BASE_TIME.plusHours(2),
            "25.0",
            "20.0",
            (short) 80,
            (short) 6,
            (short) 1006
        );

        final SliceResponse<WeatherSnapshotResponse> byTemperature = search(
            UriComponentsBuilder.fromPath("/api/snapshots")
                .queryParam("temperatureCMin", "10")
                .queryParam("temperatureCMax", "20")
                .toUriString()
        );
        assertThat(byTemperature.content())
            .extracting(WeatherSnapshotResponse::temperatureC)
            .singleElement()
            .satisfies(temperature ->
                assertThat(temperature).isEqualByComparingTo("15.0")
            );

        final SliceResponse<WeatherSnapshotResponse> byTime = search(
            UriComponentsBuilder.fromPath("/api/snapshots")
                .queryParam(
                    "snapshotTimeMin",
                    BASE_TIME.plusMinutes(30)
                )
                .queryParam(
                    "snapshotTimeMax",
                    BASE_TIME.plusHours(2).plusMinutes(30)
                )
                .toUriString()
        );
        assertThat(byTime.content()).hasSize(2);

        final SliceResponse<WeatherSnapshotResponse> byHumidity = search(
            UriComponentsBuilder.fromPath("/api/snapshots")
                .queryParam("humidityMin", "70")
                .toUriString()
        );
        assertThat(byHumidity.content())
            .extracting(WeatherSnapshotResponse::humidity)
            .containsExactly((short) 80);
    }

    @Test
    void paginatesSnapshotSearch() {
        final Location location = createLocation(
            "Berlin",
            "Europe/Berlin",
            "52.520",
            "13.405"
        );
        createSnapshot(
            location,
            BASE_TIME,
            "5.0",
            "10.0",
            (short) 40,
            (short) 1,
            (short) 1000
        );
        createSnapshot(
            location,
            BASE_TIME.plusHours(1),
            "15.0",
            "15.0",
            (short) 60,
            (short) 3,
            (short) 1003
        );
        createSnapshot(
            location,
            BASE_TIME.plusHours(2),
            "25.0",
            "20.0",
            (short) 80,
            (short) 6,
            (short) 1006
        );

        final SliceResponse<WeatherSnapshotResponse> firstPage = search(
            "/api/snapshots?page=0&size=2"
        );
        final SliceResponse<WeatherSnapshotResponse> secondPage = search(
            "/api/snapshots?page=1&size=2"
        );

        assertThat(firstPage.content()).hasSize(2);
        assertThat(firstPage.hasNext()).isTrue();
        assertThat(secondPage.content()).hasSize(1);
        assertThat(secondPage.hasNext()).isFalse();
    }

    @Test
    void rejectsInvalidSnapshotParameters() {
        assertThat(
            statusOf("/api/snapshots?size=0")
        ).isEqualTo(HttpStatus.BAD_REQUEST);

        assertThat(
            statusOf("/api/snapshots?size=101")
        ).isEqualTo(HttpStatus.BAD_REQUEST);

        assertThat(
            statusOf("/api/snapshots?page=-1")
        ).isEqualTo(HttpStatus.BAD_REQUEST);

        assertThat(
            statusOf("/api/snapshots?snapshotTimeMin=not-a-date")
        ).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void returnsNotFoundProblemForUnknownSnapshot() {
        final ResponseEntity<String> response = restTemplate.getForEntity(
            "/api/snapshots/{id}",
            String.class,
            999_999
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getHeaders().getContentType())
            .isEqualTo(MediaType.APPLICATION_PROBLEM_JSON);
        assertThat(response.getBody()).contains("Weather snapshot not found");
    }

    private SliceResponse<WeatherSnapshotResponse> search(final String url) {
        final ResponseEntity<SliceResponse<WeatherSnapshotResponse>>
            response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() { }
            );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();

        return response.getBody();
    }

    private HttpStatus statusOf(final String url) {
        return HttpStatus.valueOf(
            restTemplate.getForEntity(url, String.class)
                .getStatusCode()
                .value()
        );
    }
}
