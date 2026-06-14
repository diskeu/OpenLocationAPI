package dev.timjelenz.openlocationapi.services;

import java.util.List;

import org.springframework.stereotype.Service;

import dev.timjelenz.openlocationapi.models.UserStarredLocation;
import dev.timjelenz.openlocationapi.models.embeddables.UserIdLocationId;
import dev.timjelenz.openlocationapi.repositories.UserStarredLocationRepository;
import dev.timjelenz.openlocationapi.security.CurrentUserProvider;
import jakarta.transaction.Transactional;

@Service
public class UserStarredLocationService {
    private final LocationService locationService;
    private final UserStarredLocationRepository userStarredLocationRepository;
    private final CurrentUserProvider currentUserProvider;

    public UserStarredLocationService(
        final UserStarredLocationRepository userStarredLocationRepository,
        final LocationService locationService,
        final CurrentUserProvider currentUserProvider
    ) {
        this.locationService = locationService;
        this.userStarredLocationRepository = userStarredLocationRepository;
        this.currentUserProvider = currentUserProvider;
    }
    
    /**
     * Gets all starred locations from the current user.
     * 
     * @return all starred locations from the current user
     */
    public List<UserStarredLocation> getAllUsersStarredLocations() {
        int id = currentUserProvider.get()
            .getId();
        return userStarredLocationRepository.findById_UserId(id);
    }

    /**
     * Determines wheter a user has starred a location.
     *
     * @param id location id 
     * @return if the user has starred the location
     */
    public boolean hasStarredLocation(int id) {
        // Defining composite key
        UserIdLocationId userIdLocationId = new UserIdLocationId();
        userIdLocationId.setLocationId(id);
        userIdLocationId.setUserId(currentUserProvider.get().getId());

        return userStarredLocationRepository.existsById(userIdLocationId);
    }

    /**
     * Adds a starred location to the current user.
     *
     * @param id location id
     */
    @Transactional
    public void starLocationById(int id) {
        if (hasStarredLocation(id)) { return; }

        userStarredLocationRepository.save(
            new UserStarredLocation(
                currentUserProvider.get(),
                locationService.getLocationEntityById(id)
            )
        );
    }

    /**
     * Deletes a starred location from the current user.
     *
     * @param id location id
     */
    @Transactional
    public void unstarLocationById(int id) {
        UserIdLocationId userIdLocationId = new UserIdLocationId();
        userIdLocationId.setLocationId(id);
        userIdLocationId.setUserId(currentUserProvider.get().getId());

        userStarredLocationRepository.deleteById(userIdLocationId);
    }
}
