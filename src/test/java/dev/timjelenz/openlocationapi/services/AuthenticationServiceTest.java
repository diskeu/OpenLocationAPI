package dev.timjelenz.openlocationapi.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import dev.timjelenz.openlocationapi.exceptions.service.user.InvalidCredentialsException;
import dev.timjelenz.openlocationapi.exceptions.service.user.UserNotFoundException;
import dev.timjelenz.openlocationapi.models.User;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    void authenticatesUserWithMatchingPassword() {
        final User user = new User(
            "alice",
            "alice@example.com",
            "password-hash"
        );
        when(userService.getUserEntityByName("alice")).thenReturn(user);
        when(passwordEncoder.matches("Password1", "password-hash"))
            .thenReturn(true);

        assertThat(authenticationService.authenticate("alice", "Password1"))
            .isSameAs(user);
    }

    @Test
    void rejectsWrongPassword() {
        final User user = new User(
            "alice",
            "alice@example.com",
            "password-hash"
        );
        when(userService.getUserEntityByName("alice")).thenReturn(user);
        when(passwordEncoder.matches("WrongPass1", "password-hash"))
            .thenReturn(false);

        assertThatThrownBy(() ->
            authenticationService.authenticate("alice", "WrongPass1")
        ).isInstanceOf(InvalidCredentialsException.class);
    }

    @Test
    void propagatesUnknownUser() {
        when(userService.getUserEntityByName("unknown"))
            .thenThrow(new UserNotFoundException());

        assertThatThrownBy(() ->
            authenticationService.authenticate("unknown", "Password1")
        ).isInstanceOf(UserNotFoundException.class);
    }
}
