package dev.timjelenz.openlocationapi.services;

import org.springframework.security.crypto.password.PasswordEncoder;

import dev.timjelenz.openlocationapi.exceptions.service.user.InvalidCredentials;
import dev.timjelenz.openlocationapi.models.User;

public class AuthenticationService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(
        final UserService userService,
        final PasswordEncoder passwordEncoder
    ) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Authenticates a user.
     *
     * @param userName name of the user
     * @param password raw password of the user
     * @return user
     * @throws if the userName or the password is incorrect
     */
    public User authenticate(final String userName, final String password) {
        User user = userService.getUserEntityByName(userName);

        if (!passwordEncoder.matches(password, user.getUserPasswordHash())) {
            throw new InvalidCredentials();
        }
        return user;
    }
}
