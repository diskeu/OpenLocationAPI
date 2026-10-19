package dev.timjelenz.openlocationapi.integration;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import dev.timjelenz.openlocationapi.dto.requests.CreateUserRequest;
import dev.timjelenz.openlocationapi.dto.requests.LoginRequest;
import dev.timjelenz.openlocationapi.dto.responses.user.PublicUserResponse;

class AuthApiIT extends AbstractApiIntegrationTest {

    @Test
    void logsInWithValidCredentials() {
        restTemplate.postForEntity(
            "/api/users",
            new CreateUserRequest("alice", "alice@example.com", "Password1"),
            String.class
        );

        final ResponseEntity<PublicUserResponse> response =
            restTemplate.postForEntity(
                "/api/auth/login",
                new LoginRequest("alice", "Password1"),
                PublicUserResponse.class
            );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().userName()).isEqualTo("alice");
    }

    @Test
    void rejectsWrongPassword() {
        restTemplate.postForEntity(
            "/api/users",
            new CreateUserRequest("alice", "alice@example.com", "Password1"),
            String.class
        );

        final ResponseEntity<String> response = restTemplate.postForEntity(
            "/api/auth/login",
            new LoginRequest("alice", "WrongPass1"),
            String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void rejectsUnknownUser() {
        final ResponseEntity<String> response = restTemplate.postForEntity(
            "/api/auth/login",
            new LoginRequest("nobody", "Password1"),
            String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void protectsCurrentUserEndpointWithoutAuth() {
        final ResponseEntity<String> response = restTemplate.getForEntity(
            "/api/users/me",
            String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void allowsCurrentUserEndpointWithBasicAuth() {
        restTemplate.postForEntity(
            "/api/users",
            new CreateUserRequest("alice", "alice@example.com", "Password1"),
            String.class
        );

        final ResponseEntity<String> response = restTemplate
            .withBasicAuth("alice", "Password1")
            .getForEntity("/api/users/me", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}