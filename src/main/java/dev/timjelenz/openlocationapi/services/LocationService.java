package dev.timjelenz.openlocationapi.services;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import dev.timjelenz.openlocationapi.dto.LocationDistance;
import dev.timjelenz.openlocationapi.exceptions.service.location.LocationNotFoundException;
import dev.timjelenz.openlocationapi.models.Location;
import dev.timjelenz.openlocationapi.repositories.LocationRepository;

@Service
public class LocationService {
    private final LocationRepository locationRepository;

    public LocationService(final LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    Location getLocationEntityById(final int locationId) {
        return locationRepository.findById(locationId)
            .orElseThrow(LocationNotFoundException::new);
    }

    Location getLocationEntityByIANAName(final String ianaName) {
        return locationRepository.findByIanaName(ianaName)
            .orElseThrow(LocationNotFoundException::new);
    }

    List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    Location getExactLocation(final BigDecimal latitude, final BigDecimal longitude) {
        return locationRepository.findByLatitudeAndLongitude(latitude, longitude)
            .orElseThrow(LocationNotFoundException::new);
    }

    /**
     * Returns all locations found in the specific distance.
     * 
     * @param latitude latitude
     * @param lonitude longitude
     * @param radiusKM area where locations will be returned
     * @param pageable pageable that controls the amount of returned locations
     * @return slice of LocationDistance DTO
     */
    public Slice<LocationDistance> findNearby(
        final BigDecimal latitude,
        final BigDecimal longitude,
        final BigDecimal radiusKM,
        final Pageable pageable
    ) {
        return locationRepository.findNearby(
            latitude,
            longitude,
            radiusKM,
            pageable
        );
    }
}
