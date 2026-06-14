package dev.timjelenz.openlocationapi.services;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
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
        final int id = currentUserProvider.get()
            .getId();
        return userStarredLocationRepository.findById_UserId(id);
    }

    /**
     * Utility function to return a `UserIdLocationId` using the current user.
     * 
     * @param id location id
     * @return the corresponding `UserIdLocationId`
     */
    private UserIdLocationId getUserIdLocationId(final int id) {
        return UserIdLocationId.builder()
        .userId(currentUserProvider.get().getId())
        .locationId(id)
        .build();
    }

    /**
     * Creates and saves a starred location to the current user
     *
     * @param id location id
     */
    private void createUserStarredLocation(final int id) {
        try {
            userStarredLocationRepository.save(
                new UserStarredLocation(
                    currentUserProvider.get(),
                    locationService.getLocationEntityById(id)
                )
            );
        } catch (DataIntegrityViolationException e) { }
    }

    /**
     * Determines wheter a user has starred a location.
     *
     * @param id location id 
     * @return if the user has starred the location
     */
    public boolean hasStarredLocation(final int id) {
        return userStarredLocationRepository.existsById(
            getUserIdLocationId(id)
        );
    }

    /**
     * Adds a starred location to the current user.
     *
     * @param id location id
     */
    @Transactional
    public void starLocationById(final int id) {
        if (hasStarredLocation(id)) { return; }
        createUserStarredLocation(id);
    }

    /**
     * Deletes a starred location from the current user.
     *
     * @param id location id
     */
    @Transactional
    public void unstarLocationById(final int id) {
        userStarredLocationRepository.deleteById(
            getUserIdLocationId(id)
        );
    }
    
    /**
     * Adds or removes a star from the current user.
     * 
     * @param locationId the id of the location to star/unstar
     * @return true if the location is now starred else false
     */
    @Transactional
    public boolean toggle(final int locationId) {
        final UserIdLocationId id = getUserIdLocationId(locationId);

        if (userStarredLocationRepository.deleteByIdIfExists(id) == 0) {
            createUserStarredLocation(locationId);
            return true;
        }
        return false;
    }
}
