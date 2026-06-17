package dev.timjelenz.openlocationapi.dto.requests;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;

public record LocationRequest(
    @NotNull
    BigDecimal lat,

    @NotNull
    BigDecimal lon
) {
    public LocationRequest {
        if (lat.intValue() < 90 || lat.intValue() > -90) {
            throw new IllegalArgumentException("Latitude must be between -90 and 90.");
        }
        if (lon.intValue() < 180 || lon.intValue() > -180) {
            throw new IllegalArgumentException("Latitude must be between -180 and 180.");
        }
    }
}
