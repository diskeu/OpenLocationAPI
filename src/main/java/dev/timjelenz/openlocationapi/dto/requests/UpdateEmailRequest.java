package dev.timjelenz.openlocationapi.dto.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request to change the current user's email address.
 *
 * @param password the current password used for verification
 * @param newEmail the new email address
 */
public record UpdateEmailRequest(
    @NotBlank
    String password,

    @NotBlank
    @Email
    @Size(max = 254)
    String newEmail
) { }
