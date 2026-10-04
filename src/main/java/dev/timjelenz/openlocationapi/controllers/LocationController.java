package dev.timjelenz.openlocationapi.controllers;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.timjelenz.openlocationapi.dto.LocationDistance;
import dev.timjelenz.openlocationapi.dto.requests.Coordinates;
import dev.timjelenz.openlocationapi.dto.responses.LocationDistanceResponse;
import dev.timjelenz.openlocationapi.dto.responses.LocationResponse;
import dev.timjelenz.openlocationapi.dto.responses.SliceResponse;
import dev.timjelenz.openlocationapi.services.LocationService;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

/**
 * REST endpoints for locations.
 */
@Validated
@RestController
@RequestMapping("/api/locations")
public class LocationController {

    private final LocationService locationService;

    public LocationController(final LocationService locationService) {
        this.locationService = locationService;
    }

    /**
     * Returns all locations.
     *
     * @return all locations
     */
    @GetMapping
    public List<LocationResponse> getAllLocations() {
        return locationService.getAllLocations()
            .stream()
            .map(LocationResponse::new)
            .toList();
    }

    /**
     * Returns the location with the given id.
     *
     * @param id the location id
     * @return the location
     */
    @GetMapping("/{id}")
    public LocationResponse getLocationById(
        @PathVariable("id") final int id
    ) {
        return new LocationResponse(
            locationService.getLocationEntityById(id)
        );
    }

    /**
     * Returns the location with the given name.
     *
     * @param name the location name
     * @return the location
     */
    @GetMapping("/search")
    public LocationResponse getLocationByName(
        @RequestParam("name") final String name
    ) {
        return new LocationResponse(
            locationService.getLocationEntityByName(name)
        );
    }

    /**
     * Returns the location with the given IANA name.
     *
     * @param ianaName the IANA name
     * @return the location
     */
    @GetMapping("/iana/{ianaName}")
    public LocationResponse getLocationByIanaName(
        @PathVariable("ianaName") final String ianaName
    ) {
        return new LocationResponse(
            locationService.getLocationEntityByIANAName(ianaName)
        );
    }

    /**
     * Returns all locations within the given radius.
     *
     * @param lat latitude of the center point
     * @param lon longitude of the center point
     * @param radiusKm radius in kilometers
     * @param page page index
     * @param size page size
     * @return a slice of locations including their distance
     */
    @GetMapping("/nearby")
    public SliceResponse<LocationDistanceResponse> findNearby(
        @RequestParam("lat")
        @DecimalMin("-90") @DecimalMax("90")
        final BigDecimal lat,

        @RequestParam("lon")
        @DecimalMin("-180") @DecimalMax("180")
        final BigDecimal lon,

        @RequestParam("radiusKm")
        @DecimalMin("0")
        final BigDecimal radiusKm,

        @RequestParam(value = "page", defaultValue = "0")
        @Min(0)
        final int page,

        @RequestParam(value = "size", defaultValue = "20")
        @Min(1) @Max(100)
        final int size
    ) {
        final Coordinates coordinates = new Coordinates(lat, lon);
        final Slice<LocationDistance> locations = locationService.findNearby(
            coordinates.lat(),
            coordinates.lon(),
            radiusKm,
            PageRequest.of(page, size)
        );

        return SliceResponse.from(
            locations.map(LocationDistanceResponse::new)
        );
    }
}
