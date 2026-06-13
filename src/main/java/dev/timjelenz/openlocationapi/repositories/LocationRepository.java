package dev.timjelenz.openlocationapi.repositories;

import java.awt.print.Pageable;
import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dev.timjelenz.openlocationapi.dto.LocationDistance;
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
    
    /**
     * Finds a exact location.
     * 
     * @param latitude the exact latitude
     * @param longitude the exact longitude
     */
    Optional<Location> findByLongitudeAndLatitude(
        BigDecimal latitude,
        BigDecimal longitude
    );

    /**
     * Finds nearby locations.
     * 
     * @param latitude the latitude of the point to search nearby locations
     * @param longitude the longitude of the point to search nearby locations
     * @param offset the range to search the locations in km
     * @param pageable return only the requested item amount in the pageable
     * @return the amount of requested elements
     */
    @Query("""
        SELECT
            l,
            2 * 6371 * ASIN(
                SQRT(
                    POWER(SIN(RADIANS(l.latitude - :lat)  / 2), 2) +
                    COS(RADIANS(:lat)) * COS(RADIANS(l.latitude)) *
                    POWER(SIN(RADIANS(l.longitude - :long) / 2), 2)
                )
            ) AS distance
        FROM Location l
        WHERE distance <= :offset
    """)
    /* Returning a `Page` would require a count - query which would make the
    finding almost twice as slow. Since you can also get a `Page` behaviour
    with `Slice` by using `hasnext()`, this seems like the best approach. */
    Slice<LocationDistance> findNearby(
        final @Param("lat") BigDecimal latitude,
        final @Param("long") BigDecimal longitude,
        final @Param("offset") BigDecimal offset,
        final Pageable pageable
    );
}
