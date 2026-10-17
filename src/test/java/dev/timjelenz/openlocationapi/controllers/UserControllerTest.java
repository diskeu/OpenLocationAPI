package dev.timjelenz.openlocationapi.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import dev.timjelenz.openlocationapi.dto.requests.CreateUserRequest;
import dev.timjelenz.openlocationapi.dto.requests.UpdateUserAppereanceRequest;
import dev.timjelenz.openlocationapi.dto.responses.user.PrivateUserResponse;
import dev.timjelenz.openlocationapi.dto.responses.user.PublicUserResponse;
import dev.timjelenz.openlocationapi.services.UserService;

@AutoConfigureMockMvc(addFilters = false)
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

    @Test
    void returnsCurrentUser() throws Exception {
        when(userService.getPrivateUser()).thenReturn(
            new PrivateUserResponse(1, "alice", "alice@example.com")
        );

        mockMvc.perform(get("/api/users/me"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.userName").value("alice"))
            .andExpect(jsonPath("$.userEmail").value("alice@example.com"));

        verify(userService).getPrivateUser();
    }

    @Test
    void updatesCurrentUser() throws Exception {
        mockMvc.perform(
            put("/api/users/me")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "username": "alice2"
                    }
                    """)
        )
            .andExpect(status().isNoContent());

        verify(userService).updateUser(
            new UpdateUserAppereanceRequest("alice2")
        );
    }

    @Test
    void updatesCurrentUserPassword() throws Exception {
        mockMvc.perform(
            put("/api/users/me/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "currentPassword": "Password1",
                      "newPassword": "NewPassword1"
                    }
                    """)
        )
            .andExpect(status().isNoContent());

        verify(userService).updatePassword("Password1", "NewPassword1");
    }

    @Test
    void updatesCurrentUserEmail() throws Exception {
        mockMvc.perform(
            put("/api/users/me/email")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "password": "Password1",
                      "newEmail": "new@example.com"
                    }
                    """)
        )
            .andExpect(status().isNoContent());

        verify(userService).updateEmail("Password1", "new@example.com");
    }

    @Test
    void deletesCurrentUser() throws Exception {
        mockMvc.perform(delete("/api/users/me"))
            .andExpect(status().isNoContent());

        verify(userService).deleteUser();
    }
}
