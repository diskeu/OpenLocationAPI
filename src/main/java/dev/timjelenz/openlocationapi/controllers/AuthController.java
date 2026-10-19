package dev.timjelenz.openlocationapi.controllers;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.timjelenz.openlocationapi.dto.requests.LoginRequest;
import dev.timjelenz.openlocationapi.dto.responses.user.PublicUserResponse;
import dev.timjelenz.openlocationapi.services.UserService;
import jakarta.validation.Valid;

/**
 * REST endpoints for authentication.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    public AuthController(
        final AuthenticationManager authenticationManager,
        final UserService userService
    ) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    /**
     * Authenticates a user with the given credentials.
     *
     * @param request the login request
     * @return the authenticated user's public profile
     */
    @PostMapping("/login")
    public PublicUserResponse login(
        @Valid @RequestBody final LoginRequest request
    ) {
        final Authentication authentication =
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.username(),
                    request.password()
                )
            );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return userService.getUserByName(request.username());
    }
}