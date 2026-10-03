package dev.timjelenz.openlocationapi.dto.responses.weather;

import dev.timjelenz.openlocationapi.clients.weatherapiclient.dto.ForecastHourWeatherDTO;
import dev.timjelenz.openlocationapi.clients.weatherapiclient.dto.WeatherResponseDTO;

/**
 * Public representation of the current weather at a location.
 */
public record CurrentWeatherResponse(
    WeatherLocationResponse location,
    ForecastHourResponse current
) {
    /**
     * Maps a current weather response to its public representation.
     *
     * @param weatherResponse the current weather response
     */
    public CurrentWeatherResponse(
        final WeatherResponseDTO<ForecastHourWeatherDTO> weatherResponse
    ) {
        this(
            new WeatherLocationResponse(weatherResponse.weatherLocationDTO()),
            new ForecastHourResponse(weatherResponse.data())
        );
    }
}
