package dev.timjelenz.openlocationapi.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import dev.timjelenz.openlocationapi.exceptions.service.user.InvalidCredentialsException;
import dev.timjelenz.openlocationapi.models.User;

@Service
public final class SecurityCurrentUserProvider implements CurrentUserProvider {
    @Override
    public User get() {
        final Authentication authentication =
            SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
            throw new InvalidCredentialsException();
        }
        return (User) authentication.getPrincipal();
    }
}