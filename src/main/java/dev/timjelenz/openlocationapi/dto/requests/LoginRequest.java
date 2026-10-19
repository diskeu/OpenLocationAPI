package dev.timjelenz.openlocationapi.dto.requests;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
    @NotBlank String username,
    @NotBlank String password
) { }