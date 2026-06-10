package dev.timjelenz.openlocationapi.services;

import org.springframework.security.crypto.password.PasswordEncoder;

import dev.timjelenz.openlocationapi.exceptions.service.user.InvalidCredentials;
import dev.timjelenz.openlocationapi.models.User;
import dev.timjelenz.openlocationapi.repositories.UserRepository;

public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(
        final UserRepository userRepository,
        final PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
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
    public User authenticate(
        final String userName,
        final String password
    ) throws InvalidCredentials {
        User user = userRepository
            .findByUserName(userName)
            .orElseThrow(InvalidCredentials::new);

        if (!passwordEncoder.matches(password, user.getUserPasswordHash())) {
            throw new InvalidCredentials();
        }
        return user;
    }
}
