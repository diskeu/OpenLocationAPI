package dev.timjelenz.openlocationapi.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import dev.timjelenz.openlocationapi.dto.requests.CreateUserRequest;
import dev.timjelenz.openlocationapi.dto.responses.user.PublicUserResponse;
import dev.timjelenz.openlocationapi.services.UserService;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Test
    void createsUser() throws Exception {
        when(userService.getUserByName("alice"))
            .thenReturn(new PublicUserResponse(1, "alice"));

        mockMvc.perform(
            post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "username": "alice",
                      "email": "alice@example.com",
                      "plainPassword": "Password1"
                    }
                    """)
        )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.userName").value("alice"));

        verify(userService).createUser(any(CreateUserRequest.class));
    }

    @Test
    void rejectsInvalidRequest() throws Exception {
        mockMvc.perform(
            post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "username": "alice",
                      "email": "alice@example.com",
                      "plainPassword": "short"
                    }
                    """)
        )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errors.plainPassword").exists());
    }
}
