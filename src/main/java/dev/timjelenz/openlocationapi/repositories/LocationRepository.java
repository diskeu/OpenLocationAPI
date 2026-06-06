package dev.timjelenz.openlocationapi.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.timjelenz.openlocationapi.models.Location;

public interface LocationRepository extends JpaRepository<Location, Integer> {
    Optional<Location> findByLocationName(String locationName);

    Optional<Location> findByIanaName(String ianaName);
}
