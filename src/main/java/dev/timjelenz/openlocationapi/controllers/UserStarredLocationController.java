package dev.timjelenz.openlocationapi.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import dev.timjelenz.openlocationapi.dto.responses.StarredLocationResponse;
import dev.timjelenz.openlocationapi.models.UserStarredLocation;
import dev.timjelenz.openlocationapi.services.UserStarredLocationService;

/**
 * REST endpoints for the current user's starred locations.
 */
@RestController
@RequestMapping("/api/users/me/starred")
public class UserStarredLocationController {

    private final UserStarredLocationService userStarredLocationService;

    public UserStarredLocationController(
        final UserStarredLocationService userStarredLocationService
    ) {
        this.userStarredLocationService = userStarredLocationService;
    }

    /**
     * Returns all locations starred by the current user.
     *
     * @return the starred locations
     */
    @GetMapping
    public List<StarredLocationResponse> getStarredLocations() {
        return userStarredLocationService.getAllUsersStarredLocations()
            .stream()
            .map(StarredLocationResponse::new)
            .toList();
    }

    /**
     * Stars the given location for the current user.
     *
     * @param locationId the location id
     */
    @PostMapping("/{locationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void starLocation(
        @PathVariable("locationId") final int locationId
    ) {
        userStarredLocationService.starLocationById(locationId);
    }

    /**
     * Unstars the given location for the current user.
     *
     * @param locationId the location id
     */
    @DeleteMapping("/{locationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unstarLocation(
        @PathVariable("locationId") final int locationId
    ) {
        userStarredLocationService.unstarLocationById(locationId);
    }
}