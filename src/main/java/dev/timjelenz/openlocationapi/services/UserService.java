package dev.timjelenz.openlocationapi.services;

import java.nio.charset.StandardCharsets;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import dev.timjelenz.openlocationapi.dto.requests.CreateUserRequest;
import dev.timjelenz.openlocationapi.exceptions.service.user.UserAlreadyExists;
import dev.timjelenz.openlocationapi.models.User;
import dev.timjelenz.openlocationapi.repositories.UserRepository;
import dev.timjelenz.openlocationapi.exceptions.service.user.UserAlreadyExists;

/**
 * User Service.
 */
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    /**
     * Creates and inserts user into db.
     * 
     * @param createUserRequest will be used to create the User
     */
    public void createUser(CreateUserRequest createUserRequest) {
        final String userName = createUserRequest.username();

        if (userRepository.findByUserName(userName).isPresent()) {
            throw new UserAlreadyExists("User already Exists");
        }
        User user = new User(
            userName,
            createUserRequest.email(),
            passwordEncoder.encode(createUserRequest.plainPassword())
        );
        userRepository.save(user);
    }
}