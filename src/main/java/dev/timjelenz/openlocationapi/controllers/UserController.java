package dev.timjelenz.openlocationapi.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import dev.timjelenz.openlocationapi.dto.requests.CreateUserRequest;
import dev.timjelenz.openlocationapi.dto.requests.UpdateEmailRequest;
import dev.timjelenz.openlocationapi.dto.requests.UpdatePasswordRequest;
import dev.timjelenz.openlocationapi.dto.requests.UpdateUserAppereanceRequest;
import dev.timjelenz.openlocationapi.dto.responses.user.PrivateUserResponse;
import dev.timjelenz.openlocationapi.dto.responses.user.PublicUserResponse;
import dev.timjelenz.openlocationapi.services.UserService;
import jakarta.validation.Valid;

/**
 * REST endpoints for users.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    /**
     * Creates a new user.
     *
     * @param request the user creation request
     * @return the created user
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PublicUserResponse createUser(
        @Valid @RequestBody final CreateUserRequest request
    ) {
        userService.createUser(request);
        return userService.getUserByName(request.username());
    }

    /**
     * Returns the private profile of the current user.
     *
     * @return the current user's private profile
     */
    @GetMapping("/me")
    public PrivateUserResponse getCurrentUser() {
        return userService.getPrivateUser();
    }

    /**
     * Updates the appearance of the current user.
     *
     * @param request the update request
     */
    @PutMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCurrentUser(
        @Valid @RequestBody final UpdateUserAppereanceRequest request
    ) {
        userService.updateUser(request);
    }

    /**
     * Changes the password of the current user.
     *
     * @param request the password change request
     */
    @PutMapping("/me/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCurrentUserPassword(
        @Valid @RequestBody final UpdatePasswordRequest request
    ) {
        userService.updatePassword(
            request.currentPassword(),
            request.newPassword()
        );
    }

    /**
     * Changes the email address of the current user.
     *
     * @param request the email change request
     */
    @PutMapping("/me/email")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCurrentUserEmail(
        @Valid @RequestBody final UpdateEmailRequest request
    ) {
        userService.updateEmail(
            request.password(),
            request.newEmail()
        );
    }

    /**
     * Deletes the current user.
     */
    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCurrentUser() {
        userService.deleteUser();
    }

    /**
     * Returns the public profile of the given user.
     *
     * @param id the user id
     * @return the public user profile
     */
    @GetMapping("/{id}")
    public PublicUserResponse getUserById(
        @PathVariable("id") final int id
    ) {
        return userService.getUserById(id);
    }

    /**
     * Returns the public profile of the user with the given name.
     *
     * @param userName the user name
     * @return the public user profile
     */
    @GetMapping("/search")
    public PublicUserResponse getUserByName(
        @RequestParam("userName") final String userName
    ) {
        return userService.getUserByName(userName);
    }
}
