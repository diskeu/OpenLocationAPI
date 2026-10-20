package dev.timjelenz.openlocationapi.integration;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import dev.timjelenz.openlocationapi.dto.requests.CreateUserRequest;
import dev.timjelenz.openlocationapi.dto.responses.StarredLocationResponse;

class StarredLocationApiIT extends AbstractApiIntegrationTest {

    @Test
    void starsAndUnstarsLocationForCurrentUser() {
        final var user = restTemplate.postForEntity(
            "/api/users",
            new CreateUserRequest("alice", "alice@example.com", "Password1"),
            Void.class
        );
        assertThat(user.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        final var berlin = createLocation(
            "Berlin", "Europe/Berlin", "52.520", "13.405"
        );

        final var authed = restTemplate.withBasicAuth("alice", "Password1");

        assertThat(authed.postForEntity(
            "/api/users/me/starred/{id}", null, Void.class, berlin.getId()
        ).getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        final ResponseEntity<StarredLocationResponse[]> list =
            authed.getForEntity(
                "/api/users/me/starred",
                StarredLocationResponse[].class
            );
        assertThat(list.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(list.getBody()).isNotNull();
        assertThat(list.getBody())
            .extracting(r -> r.location().name())
            .containsExactly("Berlin");

        final ResponseEntity<Void> deleteResponse = authed.exchange(
            "/api/users/me/starred/{id}",
            org.springframework.http.HttpMethod.DELETE,
            null,
            Void.class,
            berlin.getId()
        );
        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        final ResponseEntity<StarredLocationResponse[]> after =
            authed.getForEntity(
                "/api/users/me/starred",
                StarredLocationResponse[].class
            );
        assertThat(after.getBody()).isEmpty();
    }

    @Test
    void requiresAuthentication() {
        final ResponseEntity<String> response = restTemplate.getForEntity(
            "/api/users/me/starred",
            String.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }
}