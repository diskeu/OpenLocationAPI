package dev.timjelenz.openlocationapi.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import dev.timjelenz.openlocationapi.dto.requests.CreateUserRequest;
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
