package dev.timjelenz.openlocationapi.services;

import java.util.List;

import dev.timjelenz.openlocationapi.clients.weatherapiclient.WeatherAPIClient;
import dev.timjelenz.openlocationapi.clients.weatherapiclient.dto.ForecastDayWeatherDTO;
import dev.timjelenz.openlocationapi.clients.weatherapiclient.dto.ForecastHourWeatherDTO;
import dev.timjelenz.openlocationapi.clients.weatherapiclient.dto.WeatherResponseDTO;
import dev.timjelenz.openlocationapi.dto.requests.Coordinates;
import dev.timjelenz.openlocationapi.dto.requests.DurationRequest;
import dev.timjelenz.openlocationapi.models.Location;

public class WeatherService {

    private final WeatherAPIClient weatherAPIClient;
    private final LocationService locationService;

    public WeatherService(
        final WeatherAPIClient weatherAPIClient,
        final LocationService locationService
    ) {
        this.weatherAPIClient = weatherAPIClient;
        this.locationService = locationService;
    }

    public WeatherResponseDTO<ForecastHourWeatherDTO> getCurrentWeather(int id) {

        final Location location = locationService.getLocationEntityById(id);
        
        return weatherAPIClient.getCurrentWeather(
            new Coordinates(
                location.getLatitude(),
                location.getLongitude()
            )
        );
    }
    public WeatherResponseDTO<List<ForecastDayWeatherDTO>> getForecastWeather(
        int id, DurationRequest duration
    ) {
        final Location location = locationService.getLocationEntityById(id);
        
        return weatherAPIClient.getForecastWeather(
            new Coordinates(
                location.getLatitude(),
                location.getLongitude()
            ),
            duration
        );
    }
}
