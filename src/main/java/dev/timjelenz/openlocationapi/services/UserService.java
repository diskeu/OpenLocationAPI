package dev.timjelenz.openlocationapi.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import dev.timjelenz.openlocationapi.dto.requests.CreateUserRequest;
import dev.timjelenz.openlocationapi.dto.requests.UpdateUserAppereanceRequest;
import dev.timjelenz.openlocationapi.dto.responses.user.PrivateUserResponse;
import dev.timjelenz.openlocationapi.dto.responses.user.PublicUserResponse;
import dev.timjelenz.openlocationapi.exceptions.service.IdentUpdateException;
import dev.timjelenz.openlocationapi.exceptions.service.user.InvalidCredentialsException;
import dev.timjelenz.openlocationapi.exceptions.service.user.UserAlreadyExistsException;
import dev.timjelenz.openlocationapi.exceptions.service.user.UserNotFoundException;
import dev.timjelenz.openlocationapi.models.User;
import dev.timjelenz.openlocationapi.repositories.UserRepository;
import dev.timjelenz.openlocationapi.security.CurrentUserProvider;
import jakarta.transaction.Transactional;

/**
 * User Service.
 */
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CurrentUserProvider currentUserProvider;

    public UserService(
        final UserRepository userRepository,
        final PasswordEncoder passwordEncoder,
        final CurrentUserProvider currentUserProvider
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.currentUserProvider = currentUserProvider;
    }

    /**
     * Creates and inserts user into db.
     *
     * @param createUserRequest will be used to create the User
     */
    public void createUser(final CreateUserRequest createUserRequest) {
        final String userName = createUserRequest.username();

        if (userRepository.findByUserName(userName).isPresent()) {
            throw new UserAlreadyExistsException();
        }
        User user = new User(
            userName,
            createUserRequest.email(),
            passwordEncoder.encode(createUserRequest.plainPassword())
        );
        userRepository.save(user);
    }

    private PublicUserResponse mapToPublic(final User user) {
        return new PublicUserResponse(user);
    }

    /**
     * Gets the user entity via id.
     * 
     * @param id the user's id
     * @return the user entity
     */
    User getUserEntityById(final int id) {
        return userRepository.findById(id)
            .orElseThrow(UserNotFoundException::new);
    }

    /**
     * Gets the user entity via name.
     * 
     * @param name the user's name
     * @return the user entity
     */
    User getUserEntityByName(final String name) {
        return userRepository.findByUserName(name)
            .orElseThrow(UserNotFoundException::new);
    }

    /**
     * Gets the public user DTO via id.
     *
     * @param id the user's id
     * @return a public user response DTO
     */
    public PublicUserResponse getUserById(final int id) {
        return mapToPublic(
            userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new)
        );
    }

    /**
     * Gets the public user DTO via userName.
     *
     * @param userName the userName
     * @return a public user response DTO
     */
    public PublicUserResponse getUserByName(final String userName) {
        return mapToPublic(
            userRepository.findByUserName(userName)
                .orElseThrow(UserNotFoundException::new)
        );
    }

    /**
     * Gets the private user DTO.
     *
     * @return a private user response DTO
     */
    public PrivateUserResponse getPrivateUser() {
        return new PrivateUserResponse(
            currentUserProvider.get()
        );
    }

    /**
     * Deletes the current user.
     */
    public void deleteUser() {
        currentUserProvider.get();
        userRepository.deleteById(
            currentUserProvider.get().getId()
        );
    }

    /**
     * Update the current user.
     * @param UpdateUserRequest the update request
     */
    @Transactional
    public void updateUser(final UpdateUserAppereanceRequest updateUserRequest) { 
        // Relevant if more fields to the user get added
        currentUserProvider.get()
            .setUserName(
                updateUserRequest.username()
            );
    }

    /**
     * Update the user's Password.
     * 
     * @param password the old password
     * @param newPassword the newPassword which updates the old one
     */
    @Transactional
    public void updatePassword(final String password, final String newPassword) {
        if (password.equals(newPassword)) {
            throw new IdentUpdateException();
        }
        final User user = currentUserProvider.get();

        final User dbUser = userRepository.findById(user.getId())
            .orElseThrow(UserNotFoundException::new);

        if (!passwordEncoder.matches(password, dbUser.getUserPasswordHash())) {
            throw new InvalidCredentialsException();
        }
        dbUser.setUserPasswordHash(
            passwordEncoder.encode(newPassword)
        );
        // TODO: send verification email
    }

    /**
     * Update the user's email.
     * 
     * @param password the password for verification pruposes
     * @param newEmail the new email which updates the old one
     */
    @Transactional
    public void updateEmail(final String password, final String newEmail) {
        final User user = currentUserProvider.get();

        if (user.getUserEmail().equals(newEmail)) {
            throw new IdentUpdateException();
        }
        final User dbUser = userRepository.findById(user.getId())
            .orElseThrow(UserNotFoundException::new);

        if (!passwordEncoder.matches(password, dbUser.getUserPasswordHash())) {
            throw new InvalidCredentialsException();
        }
        dbUser.setUserEmail(newEmail);
        // TODO: send verification email to new email
    }
}
