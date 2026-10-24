package dev.timjelenz.openlocationapi.tools;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import dev.timjelenz.openlocationapi.clients.weatherapiclient.dto.ForecastDayWeatherDTO;
import dev.timjelenz.openlocationapi.dto.requests.DurationRequest;
import dev.timjelenz.openlocationapi.dto.responses.LocationResponse;
import dev.timjelenz.openlocationapi.dto.responses.weather.CurrentWeatherResponse;
import dev.timjelenz.openlocationapi.models.Location;
import dev.timjelenz.openlocationapi.services.LocationService;
import dev.timjelenz.openlocationapi.services.WeatherService;

/**
 * Tools exposed to the weather assistant.
 */
@Component
public class AIWeatherTools {

    private static final int MAX_FORECAST_DAYS = 7;

    private final LocationService locationService;
    private final WeatherService weatherService;

    public AIWeatherTools(
        final LocationService locationService,
        final WeatherService weatherService
    ) {
        this.locationService = locationService;
        this.weatherService = weatherService;
    }

    /**
     * Lists all locations known to the API.
     *
     * @return all locations with their ids
     */
    @Tool(description = """
        List all locations known to the API. Each entry contains the location id,
        name, IANA timezone and coordinates. Use this tool first to find the
        location id required by the weather tools.
        """)
    public List<LocationResponse> listLocations() {
        return locationService.getAllLocations()
            .stream()
            .map(LocationResponse::new)
            .toList();
    }

    /**
     * Returns the current weather for a location.
     *
     * @param locationId the location id
     * @return the current weather
     */
    @Tool(description = """
        Get the current weather for a location. Use listLocations first if you
        do not know the location id.
        """)
    public CurrentWeatherResponse getCurrentWeather(
        @ToolParam(description = "Location id returned by listLocations")
        final int locationId
    ) {
        return new CurrentWeatherResponse(
            weatherService.getCurrentWeather(locationId)
        );
    }

    /**
     * Returns the daily forecast for an inclusive date range.
     *
     * @param locationId the location id
     * @param fromDate start date in ISO format (yyyy-MM-dd)
     * @param toDate end date in ISO format (yyyy-MM-dd), inclusive
     * @return the daily forecast
     */
    @Tool(description = """
        Get the daily forecast for a location and an inclusive date range.
        Dates use ISO format yyyy-MM-dd. Forecasts are only available from
        today up to 6 days ahead. Use listLocations first if you do not know
        the location id.
        """)
    public List<DailyForecast> getForecast(
        @ToolParam(description = "Location id returned by listLocations")
        final int locationId,

        @ToolParam(description = "Start date in ISO format, e.g. 2026-11-05")
        final String fromDate,

        @ToolParam(description = "End date in ISO format, e.g. 2026-11-06")
        final String toDate
    ) {
        final Location location = locationService.getLocationEntityById(locationId);
        final ZoneId zone = ZoneId.of(location.getIanaName());
        final LocalDate today = LocalDate.now(zone);
        final LocalDate from = LocalDate.parse(fromDate);
        final LocalDate to = LocalDate.parse(toDate);

        if (from.isAfter(to)) {
            throw new IllegalArgumentException("Start date must not be after end date.");
        }
        if (from.isBefore(today)) {
            throw new IllegalArgumentException("Forecast is only available from today.");
        }

        final long daysFromToday = ChronoUnit.DAYS.between(today, to) + 1;
        if (daysFromToday > MAX_FORECAST_DAYS) {
            throw new IllegalArgumentException(
                "Forecast is only available for the next "
                    + MAX_FORECAST_DAYS
                    + " days."
            );
        }

        /*
         * The weather API always returns the requested number of days starting
         * today. Request enough days and filter the result afterwards.
         */
        final var response = weatherService.getForecastWeather(
            locationId,
            new DurationRequest(
                today.atStartOfDay(zone),
                today.plusDays(daysFromToday).atStartOfDay(zone)
            )
        );

        return response.data()
            .stream()
            .filter(day ->
                !day.date().toLocalDate().isBefore(from)
                    && !day.date().toLocalDate().isAfter(to)
            )
            .map(DailyForecast::new)
            .toList();
    }

    /**
     * Compact daily forecast returned to the model.
     *
     * @param date the day
     * @param minTempC minimum temperature in Celsius
     * @param maxTempC maximum temperature in Celsius
     * @param avgTempC average temperature in Celsius
     * @param maxWindKMH maximum wind speed in km/h
     * @param avgHumidity average humidity in percent
     * @param uv UV index
     * @param code weather condition code
     */
    public record DailyForecast(
        LocalDateTime date,
        BigDecimal minTempC,
        BigDecimal maxTempC,
        BigDecimal avgTempC,
        BigDecimal maxWindKMH,
        int avgHumidity,
        int uv,
        int code
    ) {
        /**
         * Maps the weather API day to its compact representation.
         *
         * @param weather the API forecast day
         */
        public DailyForecast(final ForecastDayWeatherDTO weather) {
            this(
                weather.date(),
                weather.minTempC(),
                weather.maxTemC(),
                weather.avgTempC(),
                weather.maxWindKMH(),
                weather.avgHumidity(),
                weather.uv(),
                weather.code()
            );
        }
    }
}
