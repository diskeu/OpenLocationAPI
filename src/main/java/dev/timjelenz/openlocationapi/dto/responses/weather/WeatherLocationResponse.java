package dev.timjelenz.openlocationapi.dto.responses.weather;

import java.time.LocalDateTime;

import dev.timjelenz.openlocationapi.clients.weatherapiclient.dto.WeatherLocationDTO;

/**
 * Public representation of the location block of a weather response.
 */
public record WeatherLocationResponse(
    String name,
    String region,
    String country,
    String tzId,
    LocalDateTime localTime
) {
    /**
     * Maps the weather location to its response.
     *
     * @param weatherLocation the weather location
     */
    public WeatherLocationResponse(final WeatherLocationDTO weatherLocation) {
        this(
            weatherLocation.name(),
            weatherLocation.region(),
            weatherLocation.country(),
            weatherLocation.tzId(),
            weatherLocation.localTime()
        );
    }
}
