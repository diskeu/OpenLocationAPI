package dev.timjelenz.openlocationapi.repositories;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
    @Query("""
        SELECT l
        FROM Location l
        WHERE
            (l.latitude BETWEEN :latitudeMin AND :latitudeMax) AND
            (l.longitude BETWEEN :longitudeMin AND :longitudeMax)
    """)
    List<Location> findNearby(
        @Param("latitudeMin") BigDecimal latitudeMin,
        @Param("latitudeMax") BigDecimal latitudeMax,
        @Param("longitudeMin") BigDecimal longitudeMin,
        @Param("longitudeMax") BigDecimal longitudeMax
    );
}
