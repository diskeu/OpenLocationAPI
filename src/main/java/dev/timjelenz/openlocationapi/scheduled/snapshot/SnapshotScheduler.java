package dev.timjelenz.openlocationapi.scheduled.snapshot;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import dev.timjelenz.openlocationapi.models.Location;
import dev.timjelenz.openlocationapi.services.LocationService;

/**
 * Periodically requests the current weather for all known locations and
 * stores it as a weather snapshot.
 */
@Component
public class SnapshotScheduler {

    /**
     * Interval between two snapshot runs (24 hours).
     */
    public static final long SNAPSHOT_INTERVAL_MS = 86_400_000L;

    private final RequestSaveSnapshot requestSaveSnapshot;
    private final LocationService locationService;

    public SnapshotScheduler(
        final RequestSaveSnapshot requestSaveSnapshot,
        final LocationService locationService
    ) {
        this.requestSaveSnapshot = requestSaveSnapshot;
        this.locationService = locationService;
    }

    /**
     * Requests the current weather for every location currently stored in the
     * database. Locations are loaded on every run so that locations added
     * after startup are included.
     */
    @Scheduled(fixedRate = SNAPSHOT_INTERVAL_MS)
    public void saveSnapshots() {
        for (Location location : locationService.getAllLocations()) {
            requestSaveSnapshot.requestWeatherAPI(location);
        }
    }
}
