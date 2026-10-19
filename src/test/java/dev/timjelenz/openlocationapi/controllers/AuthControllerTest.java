package dev.timjelenz.openlocationapi.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import dev.timjelenz.openlocationapi.dto.responses.user.PublicUserResponse;
import dev.timjelenz.openlocationapi.models.User;
import dev.timjelenz.openlocationapi.services.UserService;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private UserService userService;

    @Test
    void logsInWithValidCredentials() throws Exception {
        final User user = new User("alice", "alice@example.com", "hash");
        final Authentication authentication =
            new UsernamePasswordAuthenticationToken(user, null);
        when(authenticationManager.authenticate(any()))
            .thenReturn(authentication);
        when(userService.getUserByName("alice"))
            .thenReturn(new PublicUserResponse(1, "alice"));

        mockMvc.perform(
            post("/api/auth/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "username": "alice",
                      "password": "Password1"
                    }
                    """)
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.userName").value("alice"));
    }

    @Test
    void rejectsInvalidCredentials() throws Exception {
        when(authenticationManager.authenticate(any()))
            .thenThrow(new org.springframework.security.authentication.BadCredentialsException("bad"));

        mockMvc.perform(
            post("/api/auth/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "username": "alice",
                      "password": "wrong"
                    }
                    """)
        )
            .andExpect(status().isUnauthorized());
    }
}