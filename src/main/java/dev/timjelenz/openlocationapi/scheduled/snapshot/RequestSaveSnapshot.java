package dev.timjelenz.openlocationapi.scheduled.snapshot;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import dev.timjelenz.openlocationapi.clients.weatherapiclient.WeatherAPIClient;
import dev.timjelenz.openlocationapi.clients.weatherapiclient.dto.ForecastHourWeatherDTO;
import dev.timjelenz.openlocationapi.clients.weatherapiclient.dto.WeatherResponseDTO;
import dev.timjelenz.openlocationapi.dto.requests.Coordinates;
import dev.timjelenz.openlocationapi.models.Location;
import dev.timjelenz.openlocationapi.services.WeatherSnapshotService;

/**
 * Fetches and stores the current weather asynchronously. This is a separate
 * bean so that Spring can apply the {@link Async} proxy.
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
    public void requestWeatherAPI(final Location location) {
        final WeatherResponseDTO<ForecastHourWeatherDTO> weatherResponse =
            weatherAPIClient.getCurrentWeather(
                new Coordinates(
                    location.getLatitude(),
                    location.getLongitude()
                )
            );

        weatherSnapshotService.saveSnapshot(
            location.getId(),
            weatherResponse
        );
    }
}
