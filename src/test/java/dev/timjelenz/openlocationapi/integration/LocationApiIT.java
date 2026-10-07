package dev.timjelenz.openlocationapi.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import dev.timjelenz.openlocationapi.dto.responses.LocationDistanceResponse;
import dev.timjelenz.openlocationapi.dto.responses.LocationResponse;
import dev.timjelenz.openlocationapi.dto.responses.SliceResponse;
import dev.timjelenz.openlocationapi.models.Location;

class LocationApiIT extends AbstractApiIntegrationTest {

    @Test
    void returnsAllLocations() {
        createLocation("Berlin", "Europe/Berlin", "52.520", "13.405");
        createLocation("Potsdam", "Europe/Prague", "52.391", "13.064");

        final ResponseEntity<LocationResponse[]> response =
            restTemplate.getForEntity(
                "/api/locations",
                LocationResponse[].class
            );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody())
            .extracting(LocationResponse::name)
            .containsExactlyInAnyOrder("Berlin", "Potsdam");
    }

    @Test
    void returnsLocationById() {
        final Location berlin = createLocation(
            "Berlin",
            "Europe/Berlin",
            "52.520",
            "13.405"
        );

        final ResponseEntity<LocationResponse> response =
            restTemplate.getForEntity(
                "/api/locations/{id}",
                LocationResponse.class,
                berlin.getId()
            );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().id()).isEqualTo(berlin.getId());
        assertThat(response.getBody().name()).isEqualTo("Berlin");
        assertThat(response.getBody().ianaName()).isEqualTo("Europe/Berlin");
        assertThat(response.getBody().latitude())
            .isEqualByComparingTo("52.520");
        assertThat(response.getBody().longitude())
            .isEqualByComparingTo("13.405");
    }

    @Test
    void searchesLocationByName() {
        createLocation("Berlin", "Europe/Berlin", "52.520", "13.405");

        final ResponseEntity<LocationResponse> response =
            restTemplate.getForEntity(
                "/api/locations/search?name={name}",
                LocationResponse.class,
                "Berlin"
            );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().name()).isEqualTo("Berlin");
    }

    @Test
    void returnsLocationByIanaName() {
        createLocation("UTC", "UTC", "0.000", "0.000");

        final ResponseEntity<LocationResponse> response =
            restTemplate.getForEntity(
                "/api/locations/iana/{ianaName}",
                LocationResponse.class,
                "UTC"
            );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().ianaName()).isEqualTo("UTC");
    }

    @Test
    void findsNearbyLocationsIncludingDistanceAndPaging() {
        createLocation("Berlin", "Europe/Berlin", "52.520", "13.405");
        createLocation("Potsdam", "Europe/Prague", "52.391", "13.064");
        createLocation("Munich", "Europe/Vienna", "48.137", "11.575");

        final ResponseEntity<SliceResponse<LocationDistanceResponse>>
            response = nearby(0, 10);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().content())
            .extracting(item -> item.location().name())
            .containsExactlyInAnyOrder("Berlin", "Potsdam");
        assertThat(response.getBody().content())
            .allSatisfy(item ->
                assertThat(item.distance())
                    .isBetween(BigDecimal.ZERO, new BigDecimal("50"))
            );
        assertThat(response.getBody().content())
            .filteredOn(item -> item.location().name().equals("Potsdam"))
            .singleElement()
            .satisfies(item ->
                assertThat(item.distance())
                    .isBetween(
                        new BigDecimal("20"),
                        new BigDecimal("35")
                    )
            );

        final SliceResponse<LocationDistanceResponse> firstPage =
            nearby(0, 1).getBody();
        final SliceResponse<LocationDistanceResponse> secondPage =
            nearby(1, 1).getBody();

        assertThat(firstPage).isNotNull();
        assertThat(secondPage).isNotNull();
        assertThat(firstPage.content()).hasSize(1);
        assertThat(firstPage.hasNext()).isTrue();
        assertThat(secondPage.content()).hasSize(1);
        assertThat(secondPage.hasNext()).isFalse();
    }

    @Test
    void returnsNotFoundProblemForUnknownLocation() {
        final ResponseEntity<String> response = restTemplate.getForEntity(
            "/api/locations/{id}",
            String.class,
            999_999
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getHeaders().getContentType())
            .isEqualTo(MediaType.APPLICATION_PROBLEM_JSON);
        assertThat(response.getBody()).contains("Location not found");
    }

    @Test
    void rejectsInvalidNearbyParameters() {
        assertThat(
            restTemplate.getForEntity(
                "/api/locations/nearby"
                    + "?lat=91&lon=0&radiusKm=10",
                String.class
            ).getStatusCode()
        ).isEqualTo(HttpStatus.BAD_REQUEST);

        assertThat(
            restTemplate.getForEntity(
                "/api/locations/nearby"
                    + "?lat=0&lon=0&radiusKm=-1",
                String.class
            ).getStatusCode()
        ).isEqualTo(HttpStatus.BAD_REQUEST);

        assertThat(
            restTemplate.getForEntity(
                "/api/locations/nearby"
                    + "?lat=0&lon=0&radiusKm=10&page=-1",
                String.class
            ).getStatusCode()
        ).isEqualTo(HttpStatus.BAD_REQUEST);

        assertThat(
            restTemplate.getForEntity(
                "/api/locations/nearby"
                    + "?lat=0&lon=0&radiusKm=10&size=0",
                String.class
            ).getStatusCode()
        ).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<SliceResponse<LocationDistanceResponse>> nearby(
        final int page,
        final int size
    ) {
        return restTemplate.exchange(
            "/api/locations/nearby"
                + "?lat=52.520&lon=13.405&radiusKm=50"
                + "&page={page}&size={size}",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<>() { },
            page,
            size
        );
    }
}
