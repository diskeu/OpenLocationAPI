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
     * Snapshot CRON expression for a daily run at 00:44.
     */
    public static final String SNAPSHOT_CRON_EXPR = "0 44 0 * * ?";

    /**
     * Time zone used by the snapshot CRON expression.
     */
    public static final String SNAPSHOT_CRON_ZONE = "Europe/Vienna";

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
    @Scheduled(cron = SNAPSHOT_CRON_EXPR, zone = SNAPSHOT_CRON_ZONE)
    public void saveSnapshots() {
        for (Location location : locationService.getAllLocations()) {
            requestSaveSnapshot.requestWeatherAPI(location);
        }
    }
}
