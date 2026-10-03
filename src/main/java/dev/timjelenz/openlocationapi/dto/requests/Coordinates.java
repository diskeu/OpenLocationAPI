package dev.timjelenz.openlocationapi.dto.requests;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;

public record Coordinates(
    @NotNull
    BigDecimal lat,

    @NotNull
    BigDecimal lon
) {
    private static final BigDecimal MAX_LATITUDE = BigDecimal.valueOf(90);
    private static final BigDecimal MIN_LATITUDE = BigDecimal.valueOf(-90);
    private static final BigDecimal MAX_LONGITUDE = BigDecimal.valueOf(180);
    private static final BigDecimal MIN_LONGITUDE = BigDecimal.valueOf(-180);

    public Coordinates {
        if (lat == null || lon == null) {
            throw new IllegalArgumentException(
                "Latitude and longitude must not be null."
            );
        }
        if (lat.compareTo(MAX_LATITUDE) > 0
            || lat.compareTo(MIN_LATITUDE) < 0) {
            throw new IllegalArgumentException(
                "Latitude must be between -90 and 90."
            );
        }
        if (lon.compareTo(MAX_LONGITUDE) > 0
            || lon.compareTo(MIN_LONGITUDE) < 0) {
            throw new IllegalArgumentException(
                "Longitude must be between -180 and 180."
            );
        }
    }
}
