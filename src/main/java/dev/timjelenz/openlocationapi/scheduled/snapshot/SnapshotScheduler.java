package dev.timjelenz.openlocationapi.scheduled.snapshot;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import dev.timjelenz.openlocationapi.dto.requests.Coordinates;
import dev.timjelenz.openlocationapi.models.Location;
import dev.timjelenz.openlocationapi.services.LocationService;

@Component
public class SnapshotScheduler {

    private final RequestSaveSnapshot requestSaveSnapshot;
    private final List<Location> cachedLocations;

    public SnapshotScheduler(
        final RequestSaveSnapshot requestSaveSnapshot,
        final LocationService locationService
    ) {
        this.requestSaveSnapshot = requestSaveSnapshot;
        this.cachedLocations = locationService.getAllLocations();
    }

    @Scheduled(fixedRate = 8_640_000)
    public void safeSnapshots() {
        for (Location location : cachedLocations) {
            requestSaveSnapshot.requestWeatherAPI(
                new Coordinates(
                    location.getLatitude(),
                    location.getLongitude()
                )
            );
        }
    }
}
