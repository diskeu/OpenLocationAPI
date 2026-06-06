package dev.timjelenz.openlocationapi.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.timjelenz.openlocationapi.models.UserStarredLocation;
import dev.timjelenz.openlocationapi.models.embeddables.UserIdLocationId;

public interface UserStarredLocationRepository
extends JpaRepository<UserStarredLocation, UserIdLocationId> {
    /**
     * Find StarredLocations of the user with the given userId.
     *
     * @param userId id of the user
     * @return list of `UserStarredLocations` that match the userId
     */
    List<UserStarredLocation> findById_UserId(int userId);

    /**
     * Find StarredLocations from a locationId.
     *
     * @param locationId id of the location
     * @return list of `UserStarredLocations` that match the locationId
     */
    List<UserStarredLocation> findById_LocationId(int locationId);
}
