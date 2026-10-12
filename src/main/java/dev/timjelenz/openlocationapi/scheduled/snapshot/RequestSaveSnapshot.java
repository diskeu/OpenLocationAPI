package dev.timjelenz.openlocationapi.scheduled.snapshot;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import dev.timjelenz.openlocationapi.clients.weatherapiclient.WeatherAPIClient;
import dev.timjelenz.openlocationapi.dto.requests.Coordinates;
import dev.timjelenz.openlocationapi.services.WeatherSnapshotService;

/**
 * Since calling `requestWeatherAPI` asynchron in `SnapshotScheduler` isn`t possible,
 * this class provides `requestWeatherAPI`.
 */
@Component
public class RequestSaveSnapshot {

    private final WeatherAPIClient weatherAPIClient;
    private final WeatherSnapshotService weatherSnapshotService;

    public RequestSaveSnapshot(
        final WeatherAPIClient weatherAPIClient,
        final WeatherSnapshotService weatherSnapshotService
    ) {
        this.weatherAPIClient = weatherAPIClient;
        this.weatherSnapshotService = weatherSnapshotService;
    }

    @Async
    public void requestWeatherAPI(Coordinates coordinates) {
        weatherSnapshotService.saveSnapshot(
            weatherAPIClient.getCurrentWeather(coordinates)
        );
    }
}
