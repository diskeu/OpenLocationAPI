package dev.timjelenz.openlocationapi.dto.requests;

import jakarta.validation.constraints.Size;

public record UpdateUserAppereanceRequest(
    @Size(min = 4, max = 12)
    String username
) { }
