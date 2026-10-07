package dev.timjelenz.openlocationapi.integration;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import dev.timjelenz.openlocationapi.dto.responses.LocationResponse;

class ApplicationStartupIT extends AbstractApiIntegrationTest {

    @Test
    void startsApplicationAndServesLocations() {
        final ResponseEntity<LocationResponse[]> response =
            restTemplate.getForEntity(
                "/api/locations",
                LocationResponse[].class
            );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEmpty();
    }
}
