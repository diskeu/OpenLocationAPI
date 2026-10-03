package dev.timjelenz.openlocationapi.dto.responses;

import java.math.BigDecimal;

import dev.timjelenz.openlocationapi.dto.LocationDistance;

/**
 * Public representation of a location including its distance
 * to the requested point.
 */
public record LocationDistanceResponse(
    LocationResponse location,
    BigDecimal distance
) {
    /**
     * Maps a location distance projection to its response.
     *
     * @param locationDistance the location distance projection
     */
    public LocationDistanceResponse(final LocationDistance locationDistance) {
        this(
            new LocationResponse(locationDistance.location()),
            locationDistance.distance()
        );
    }
}
