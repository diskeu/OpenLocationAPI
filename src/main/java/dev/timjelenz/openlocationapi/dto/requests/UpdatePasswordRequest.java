package dev.timjelenz.openlocationapi.dto.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Request to change the current user's password.
 *
 * @param currentPassword the current password used for verification
 * @param newPassword the new password
 */
public record UpdatePasswordRequest(
    @NotBlank
    String currentPassword,

    @NotBlank
    @Size(min = 8, max = 24)
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).+$")
    String newPassword
) { }
