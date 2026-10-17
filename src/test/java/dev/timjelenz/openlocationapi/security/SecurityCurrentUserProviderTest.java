package dev.timjelenz.openlocationapi.security;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import dev.timjelenz.openlocationapi.models.User;

class SecurityCurrentUserProviderTest {

    @AfterEach
    void clearsSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void returnsPrincipalFromSecurityContext() {
        final User user = new User(
            "alice",
            "alice@example.com",
            "password-hash"
        );
        SecurityContextHolder.getContext().setAuthentication(
            new UsernamePasswordAuthenticationToken(user, null, List.of())
        );

        assertThat(new SecurityCurrentUserProvider().get()).isSameAs(user);
    }
}
