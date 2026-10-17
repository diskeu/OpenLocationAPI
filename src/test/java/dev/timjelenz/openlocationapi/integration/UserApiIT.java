package dev.timjelenz.openlocationapi.integration;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import dev.timjelenz.openlocationapi.dto.requests.CreateUserRequest;
import dev.timjelenz.openlocationapi.dto.responses.user.PublicUserResponse;
import dev.timjelenz.openlocationapi.models.User;

class UserApiIT extends AbstractApiIntegrationTest {

    @Test
    void createsUserAndPersistsHashedPassword() {
        final CreateUserRequest request = new CreateUserRequest(
            "alice",
            "alice@example.com",
            "Password1"
        );

        final ResponseEntity<PublicUserResponse> response =
            restTemplate.postForEntity(
                "/api/users",
                request,
                PublicUserResponse.class
            );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().userName()).isEqualTo("alice");

        final User user = userRepository.findByUserName("alice")
            .orElseThrow();
        assertThat(user.getId()).isEqualTo(response.getBody().id());
        assertThat(user.getUserEmail()).isEqualTo("alice@example.com");
        assertThat(user.getUserPasswordHash())
            .isNotEqualTo("Password1")
            .startsWith("$2");
    }

    @Test
    void returnsPublicUserByIdAndName() {
        final User user = createUser("alice", "alice@example.com");

        final ResponseEntity<PublicUserResponse> byId =
            restTemplate.getForEntity(
                "/api/users/{id}",
                PublicUserResponse.class,
                user.getId()
            );

        assertThat(byId.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(byId.getBody()).isNotNull();
        assertThat(byId.getBody().id()).isEqualTo(user.getId());
        assertThat(byId.getBody().userName()).isEqualTo("alice");

        final ResponseEntity<PublicUserResponse> byName =
            restTemplate.getForEntity(
                "/api/users/search?userName={userName}",
                PublicUserResponse.class,
                "alice"
            );

        assertThat(byName.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(byName.getBody()).isNotNull();
        assertThat(byName.getBody().id()).isEqualTo(user.getId());
    }

    @Test
    void rejectsDuplicateUserName() {
        final CreateUserRequest first = new CreateUserRequest(
            "alice",
            "alice@example.com",
            "Password1"
        );
        restTemplate.postForEntity("/api/users", first, String.class);

        final ResponseEntity<String> duplicate =
            restTemplate.postForEntity(
                "/api/users",
                new CreateUserRequest(
                    "alice",
                    "other@example.com",
                    "Password1"
                ),
                String.class
            );

        assertThat(duplicate.getStatusCode())
            .isEqualTo(HttpStatus.CONFLICT);
        assertThat(duplicate.getHeaders().getContentType())
            .isEqualTo(MediaType.APPLICATION_PROBLEM_JSON);
        assertThat(duplicate.getBody()).contains("existing data");
    }

    @Test
    void rejectsInvalidRegistrationRequest() {
        final ResponseEntity<String> response = restTemplate.postForEntity(
            "/api/users",
            new CreateUserRequest(
                "alice",
                "alice@example.com",
                "short"
            ),
            String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getHeaders().getContentType())
            .isEqualTo(MediaType.APPLICATION_PROBLEM_JSON);
        assertThat(response.getBody())
            .contains("\"errors\"")
            .contains("plainPassword");
    }

    @Test
    void returnsNotFoundProblemForUnknownUser() {
        final ResponseEntity<String> response = restTemplate.getForEntity(
            "/api/users/{id}",
            String.class,
            999_999
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getHeaders().getContentType())
            .isEqualTo(MediaType.APPLICATION_PROBLEM_JSON);
        assertThat(response.getBody()).contains("User not found");
    }

    @Test
    void rejectsDuplicateEmail() {
        restTemplate.postForEntity(
            "/api/users",
            new CreateUserRequest(
                "alice",
                "shared@example.com",
                "Password1"
            ),
            String.class
        );

        final ResponseEntity<String> duplicate =
            restTemplate.postForEntity(
                "/api/users",
                new CreateUserRequest(
                    "bobby",
                    "shared@example.com",
                    "Password1"
                ),
                String.class
            );

        assertThat(duplicate.getStatusCode())
            .isEqualTo(HttpStatus.CONFLICT);
        assertThat(duplicate.getHeaders().getContentType())
            .isEqualTo(MediaType.APPLICATION_PROBLEM_JSON);
        assertThat(duplicate.getBody()).contains("existing data");
    }

    @Test
    void rejectsInvalidEmail() {
        final ResponseEntity<String> response = restTemplate.postForEntity(
            "/api/users",
            new CreateUserRequest(
                "alice",
                "not-an-email",
                "Password1"
            ),
            String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getHeaders().getContentType())
            .isEqualTo(MediaType.APPLICATION_PROBLEM_JSON);
        assertThat(response.getBody())
            .contains("\"errors\"")
            .contains("email");
    }

    @Test
    void doesNotExposePasswordHashInPublicProfile() {
        final User user = createUser("alice", "alice@example.com");

        final ResponseEntity<String> response = restTemplate.getForEntity(
            "/api/users/{id}",
            String.class,
            user.getId()
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody())
            .doesNotContain("userPasswordHash")
            .doesNotContain("$2")
            .doesNotContain("alice@example.com");
    }
}
