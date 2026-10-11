package dev.timjelenz.openlocationapi.scheduled;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import dev.timjelenz.openlocationapi.clients.weatherapiclient.WeatherAPIClient;
import dev.timjelenz.openlocationapi.dto.requests.Coordinates;
import dev.timjelenz.openlocationapi.models.Location;
import dev.timjelenz.openlocationapi.services.LocationService;
import dev.timjelenz.openlocationapi.services.WeatherSnapshotService;

@Component
public class SnapshotScheduler {

    private final WeatherAPIClient weatherAPIClient;
    private final WeatherSnapshotService weatherSnapshotService;

    private final List<Location> cachedLocations;

    @Autowired
    private SnapshotScheduler self;

    public SnapshotScheduler(
        final WeatherAPIClient weatherAPIClient,
        final WeatherSnapshotService weatherSnapshotService,
        final LocationService locationService
    ) {
        this.weatherAPIClient = weatherAPIClient;
        this.weatherSnapshotService = weatherSnapshotService;
        this.cachedLocations = locationService.getAllLocations();
    }

    @Async
    public void requestWeatherAPI(Coordinates coordinates) {
        weatherSnapshotService.saveSnapshot(
            weatherAPIClient.getCurrentWeather(coordinates)
        );
    }

    @Scheduled(fixedRate = 84_000_000)
    public void safeSnapshots() {
        for (Location location : cachedLocations) {
            self.requestWeatherAPI(
                new Coordinates(
                    location.getLatitude(),
                    location.getLongitude()
                )
            );
        }
    }
}
