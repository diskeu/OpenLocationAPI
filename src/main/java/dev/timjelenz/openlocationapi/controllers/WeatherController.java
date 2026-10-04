package dev.timjelenz.openlocationapi.controllers;

import java.time.ZonedDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.timjelenz.openlocationapi.dto.requests.DurationRequest;
import dev.timjelenz.openlocationapi.dto.responses.weather.CurrentWeatherResponse;
import dev.timjelenz.openlocationapi.dto.responses.weather.ForecastResponse;
import dev.timjelenz.openlocationapi.services.WeatherService;

/**
 * REST endpoints for the weather of a location.
 */
@RestController
@RequestMapping("/api/locations/{locationId}/weather")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(final WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    /**
     * Returns the current weather of the given location.
     *
     * @param locationId the location id
     * @return the current weather
     */
    @GetMapping
    public CurrentWeatherResponse getCurrentWeather(
        @PathVariable("locationId") final int locationId
    ) {
        return new CurrentWeatherResponse(
            weatherService.getCurrentWeather(locationId)
        );
    }

    /**
     * Returns the weather forecast of the given location.
     *
     * @param locationId the location id
     * @param from start of the requested timespan
     * @param to end of the requested timespan
     * @return the forecast
     */
    @GetMapping("/forecast")
    public ForecastResponse getForecast(
        @PathVariable("locationId") final int locationId,

        @RequestParam("from")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        final ZonedDateTime from,

        @RequestParam("to")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        final ZonedDateTime to
    ) {
        return new ForecastResponse(
            weatherService.getForecastWeather(
                locationId,
                new DurationRequest(from, to)
            )
        );
    }
}
