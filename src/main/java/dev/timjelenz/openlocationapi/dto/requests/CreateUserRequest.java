package dev.timjelenz.openlocationapi.dto.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(
    @NotBlank
    @Size(min = 4, max = 12)
    String username,

    @Email
    @Size(max = 254)
    String email,
    
    @NotBlank
    @Size(min = 8, max = 24)
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).+$")
    String plainPassword
) { }