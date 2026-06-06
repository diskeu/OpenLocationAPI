package dev.timjelenz.openlocationapi.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.timjelenz.openlocationapi.models.UserStarredLocation;
import dev.timjelenz.openlocationapi.models.embeddables.UserIdLocationId;

public interface UserStarredLocationRepository extends JpaRepository<UserStarredLocation, UserIdLocationId> {
    public List<UserStarredLocation> findById_UserId(int userId);

    public List<UserStarredLocation> findById_LocationId(int locationId);
}
