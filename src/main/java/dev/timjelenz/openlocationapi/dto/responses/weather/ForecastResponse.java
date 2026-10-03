package dev.timjelenz.openlocationapi.dto.responses.weather;

import java.util.List;

import dev.timjelenz.openlocationapi.clients.weatherapiclient.dto.ForecastDayWeatherDTO;
import dev.timjelenz.openlocationapi.clients.weatherapiclient.dto.WeatherResponseDTO;

/**
 * Public representation of a weather forecast at a location.
 */
public record ForecastResponse(
    WeatherLocationResponse location,
    List<ForecastDayResponse> forecast
) {
    /**
     * Maps a forecast weather response to its public representation.
     *
     * @param weatherResponse the forecast weather response
     */
    public ForecastResponse(
        final WeatherResponseDTO<List<ForecastDayWeatherDTO>> weatherResponse
    ) {
        this(
            new WeatherLocationResponse(weatherResponse.weatherLocationDTO()),
            weatherResponse.data()
                .stream()
                .map(ForecastDayResponse::new)
                .toList()
        );
    }
}
