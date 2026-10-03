package dev.timjelenz.openlocationapi.dto.responses;

import dev.timjelenz.openlocationapi.models.UserStarredLocation;

/**
 * Public representation of a location starred by a user.
 */
public record StarredLocationResponse(LocationResponse location) {
    /**
     * Maps a starred location entity to its response.
     *
     * @param starredLocation the starred location entity
     */
    public StarredLocationResponse(
        final UserStarredLocation starredLocation
    ) {
        this(new LocationResponse(starredLocation.getLocation()));
    }
}
