package dev.timjelenz.openlocationapi.integration;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import dev.timjelenz.openlocationapi.dto.requests.CreateUserRequest;
import dev.timjelenz.openlocationapi.dto.requests.UpdateEmailRequest;
import dev.timjelenz.openlocationapi.dto.requests.UpdatePasswordRequest;
import dev.timjelenz.openlocationapi.dto.requests.UpdateUserAppereanceRequest;
import dev.timjelenz.openlocationapi.dto.responses.user.PrivateUserResponse;
import dev.timjelenz.openlocationapi.dto.responses.user.PublicUserResponse;
import dev.timjelenz.openlocationapi.models.Location;
import dev.timjelenz.openlocationapi.models.User;
import dev.timjelenz.openlocationapi.models.UserStarredLocation;
import dev.timjelenz.openlocationapi.models.embeddables.UserIdLocationId;
import dev.timjelenz.openlocationapi.repositories.UserRepository;
import dev.timjelenz.openlocationapi.repositories.UserStarredLocationRepository;
import dev.timjelenz.openlocationapi.security.CurrentUserProvider;

/**
 * Integration tests for the endpoints that operate on the current user.
 *
 * <p>Authentication is not implemented yet, so the current user is provided
 * by a test bean. The HTTP layer, service layer and database are real.</p>
 */
@Import(UserMeApiIT.CurrentUserTestConfiguration.class)
class UserMeApiIT extends AbstractApiIntegrationTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserStarredLocationRepository userStarredLocationRepository;

    @Test
    void returnsPrivateProfileOfCurrentUser() {
        final User alice = registerAlice("Password1");

        final ResponseEntity<PrivateUserResponse> response =
            restTemplate.getForEntity(
                "/api/users/me",
                PrivateUserResponse.class
            );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(
            new PrivateUserResponse(
                alice.getId(),
                "alice",
                "alice@example.com"
            )
        );
    }

    @Test
    void updatesCurrentUserName() {
        registerAlice("Password1");

        final ResponseEntity<Void> response = restTemplate.exchange(
            "/api/users/me",
            HttpMethod.PUT,
            new HttpEntity<>(new UpdateUserAppereanceRequest("alice2")),
            Void.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(userRepository.findByUserName("alice2")).isPresent();
        assertThat(userRepository.findByUserName("alice")).isEmpty();
    }

    @Test
    void updatesCurrentUserPassword() {
        final User alice = registerAlice("Password1");

        final ResponseEntity<Void> response = restTemplate.exchange(
            "/api/users/me/password",
            HttpMethod.PUT,
            new HttpEntity<>(
                new UpdatePasswordRequest("Password1", "NewPassword1")
            ),
            Void.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        final User updated = userRepository.findById(alice.getId())
            .orElseThrow();
        assertThat(passwordEncoder.matches(
            "NewPassword1",
            updated.getUserPasswordHash()
        )).isTrue();
    }

    @Test
    void updatesCurrentUserEmail() {
        final User alice = registerAlice("Password1");

        final ResponseEntity<Void> response = restTemplate.exchange(
            "/api/users/me/email",
            HttpMethod.PUT,
            new HttpEntity<>(
                new UpdateEmailRequest("Password1", "new@example.com")
            ),
            Void.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        final User updated = userRepository.findById(alice.getId())
            .orElseThrow();
        assertThat(updated.getUserEmail()).isEqualTo("new@example.com");
    }

    @Test
    void deletesCurrentUser() {
        registerAlice("Password1");

        final ResponseEntity<Void> response = restTemplate.exchange(
            "/api/users/me",
            HttpMethod.DELETE,
            HttpEntity.EMPTY,
            Void.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(userRepository.findByUserName("alice")).isEmpty();
    }

    @Test
    void deletesCurrentUserWithStarredLocations() {
        final User alice = registerAlice("Password1");
        final Location berlin = createLocation(
            "Berlin",
            "Europe/Berlin",
            "52.520",
            "13.405"
        );
        final UserIdLocationId starredId = UserIdLocationId.builder()
            .userId(alice.getId())
            .locationId(berlin.getId())
            .build();

        userStarredLocationRepository.saveAndFlush(
            new UserStarredLocation(alice, berlin)
        );
        assertThat(userStarredLocationRepository.findById(starredId))
            .isPresent();

        final ResponseEntity<Void> response = restTemplate.exchange(
            "/api/users/me",
            HttpMethod.DELETE,
            HttpEntity.EMPTY,
            Void.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(userRepository.findByUserName("alice")).isEmpty();
        assertThat(userStarredLocationRepository.findById(starredId))
            .isEmpty();
    }

    @Test
    void rejectsWrongCurrentPasswordForPasswordChange() {
        registerAlice("Password1");

        final ResponseEntity<String> response = restTemplate.exchange(
            "/api/users/me/password",
            HttpMethod.PUT,
            new HttpEntity<>(
                new UpdatePasswordRequest("WrongPass1", "NewPassword1")
            ),
            String.class
        );

        assertThat(response.getStatusCode())
            .isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getHeaders().getContentType())
            .isEqualTo(MediaType.APPLICATION_PROBLEM_JSON);
        assertThat(response.getBody()).contains("Invalid credentials");
    }

    @Test
    void rejectsSamePassword() {
        registerAlice("Password1");

        final ResponseEntity<String> response = restTemplate.exchange(
            "/api/users/me/password",
            HttpMethod.PUT,
            new HttpEntity<>(
                new UpdatePasswordRequest("Password1", "Password1")
            ),
            String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getHeaders().getContentType())
            .isEqualTo(MediaType.APPLICATION_PROBLEM_JSON);
        assertThat(response.getBody()).contains("Identity can not be updated");
    }

    @Test
    void rejectsWrongCurrentPasswordForEmailChange() {
        registerAlice("Password1");

        final ResponseEntity<String> response = restTemplate.exchange(
            "/api/users/me/email",
            HttpMethod.PUT,
            new HttpEntity<>(
                new UpdateEmailRequest("WrongPass1", "new@example.com")
            ),
            String.class
        );

        assertThat(response.getStatusCode())
            .isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getHeaders().getContentType())
            .isEqualTo(MediaType.APPLICATION_PROBLEM_JSON);
        assertThat(response.getBody()).contains("Invalid credentials");
    }

    @Test
    void rejectsSameEmail() {
        registerAlice("Password1");

        final ResponseEntity<String> response = restTemplate.exchange(
            "/api/users/me/email",
            HttpMethod.PUT,
            new HttpEntity<>(
                new UpdateEmailRequest("Password1", "alice@example.com")
            ),
            String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getHeaders().getContentType())
            .isEqualTo(MediaType.APPLICATION_PROBLEM_JSON);
        assertThat(response.getBody()).contains("Identity can not be updated");
    }

    @Test
    void rejectsDuplicateUserNameOnUpdate() {
        registerAlice("Password1");
        final ResponseEntity<PublicUserResponse> bob =
            restTemplate.postForEntity(
                "/api/users",
                new CreateUserRequest(
                    "bobby",
                    "bobby@example.com",
                    "Password1"
                ),
                PublicUserResponse.class
            );
        assertThat(bob.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        final ResponseEntity<String> response = restTemplate.exchange(
            "/api/users/me",
            HttpMethod.PUT,
            new HttpEntity<>(new UpdateUserAppereanceRequest("bobby")),
            String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getHeaders().getContentType())
            .isEqualTo(MediaType.APPLICATION_PROBLEM_JSON);
        assertThat(response.getBody()).contains("existing data");
        assertThat(userRepository.findByUserName("alice")).isPresent();
    }

    @Test
    void rejectsInvalidCurrentUserName() {
        registerAlice("Password1");

        final ResponseEntity<String> response = restTemplate.exchange(
            "/api/users/me",
            HttpMethod.PUT,
            new HttpEntity<>(new UpdateUserAppereanceRequest(" ")),
            String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getHeaders().getContentType())
            .isEqualTo(MediaType.APPLICATION_PROBLEM_JSON);
        assertThat(response.getBody()).contains("username");
    }

    @Test
    void rejectsWeakNewPassword() {
        registerAlice("Password1");

        final ResponseEntity<String> response = restTemplate.exchange(
            "/api/users/me/password",
            HttpMethod.PUT,
            new HttpEntity<>(
                new UpdatePasswordRequest("Password1", "short")
            ),
            String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getHeaders().getContentType())
            .isEqualTo(MediaType.APPLICATION_PROBLEM_JSON);
        assertThat(response.getBody()).contains("newPassword");
    }

    @Test
    void rejectsInvalidNewEmail() {
        registerAlice("Password1");

        final ResponseEntity<String> response = restTemplate.exchange(
            "/api/users/me/email",
            HttpMethod.PUT,
            new HttpEntity<>(
                new UpdateEmailRequest("Password1", "not-an-email")
            ),
            String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getHeaders().getContentType())
            .isEqualTo(MediaType.APPLICATION_PROBLEM_JSON);
        assertThat(response.getBody()).contains("newEmail");
    }

    @Test
    void doesNotExposePasswordHashInPrivateProfile() {
        registerAlice("Password1");

        final ResponseEntity<String> response = restTemplate.getForEntity(
            "/api/users/me",
            String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody())
            .contains("alice@example.com")
            .doesNotContain("userPasswordHash")
            .doesNotContain("$2")
            .doesNotContain("Password1");
    }

    private User registerAlice(final String password) {
        final ResponseEntity<PublicUserResponse> response =
            restTemplate.postForEntity(
                "/api/users",
                new CreateUserRequest(
                    "alice",
                    "alice@example.com",
                    password
                ),
                PublicUserResponse.class
            );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        return userRepository.findByUserName("alice").orElseThrow();
    }

    @TestConfiguration(proxyBeanMethods = false)
    static class CurrentUserTestConfiguration {

        @Bean
        @Primary
        CurrentUserProvider currentUserProvider(
            final UserRepository userRepository
        ) {
            return () -> userRepository.findByUserName("alice")
                .orElseThrow();
        }
    }
}
