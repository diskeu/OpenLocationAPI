package dev.timjelenz.openlocationapi.repositories;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.timjelenz.openlocationapi.models.Location;

public interface LocationRepository extends JpaRepository<Location, Integer> {
    /**
     * Find Location with the given name.
     *
     * @param locationName name of the Location
     * @return a optional containing a location if one was found
     */
    Optional<Location> findByLocationName(String locationName);

    /**
     * Find Location with the given `IANA` name.
     *
     * @param ianaName `IANA` name of the Location
     * @return a optional containing a location if one was found
     */
    Optional<Location> findByIanaName(String ianaName);
    
    Optional<Location> findByLongitudeAndLatitude(
        BigDecimal latitude,
        BigDecimal longitude
    );
}
