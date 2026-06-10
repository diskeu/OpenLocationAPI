package dev.timjelenz.openlocationapi.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import dev.timjelenz.openlocationapi.models.User;

@Service
public class SecurityCurrentUserProvider implements CurrentUserProvider {
    @Override
    public User get() {
        return (User) SecurityContextHolder.getContext()
            .getAuthentication()
            .getPrincipal();
    }
}
