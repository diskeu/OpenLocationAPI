package dev.timjelenz.openlocationapi.dto.responses;

import java.math.BigDecimal;

import dev.timjelenz.openlocationapi.models.Location;

/**
 * Public representation of a location.
 */
public record LocationResponse(
    int id,
    String name,
    String ianaName,
    BigDecimal latitude,
    BigDecimal longitude
) {
    /**
     * Maps a location entity to its response.
     *
     * @param location the location entity
     */
    public LocationResponse(final Location location) {
        this(
            location.getId(),
            location.getLocationName(),
            location.getIanaName(),
            location.getLatitude(),
            location.getLongitude()
        );
    }
}
