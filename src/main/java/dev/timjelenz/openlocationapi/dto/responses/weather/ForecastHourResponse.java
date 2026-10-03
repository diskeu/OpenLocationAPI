package dev.timjelenz.openlocationapi.dto.responses.weather;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import dev.timjelenz.openlocationapi.clients.weatherapiclient.dto.ForecastHourWeatherDTO;

/**
 * Public representation of the weather at a specific hour.
 */
public record ForecastHourResponse(
    LocalDateTime date,
    long dateEpoch,
    short code,
    BigDecimal temperatureC,
    BigDecimal windKMH,
    BigDecimal windDegree,
    short humidity,
    short cloud,
    BigDecimal feelsLikeC,
    short uv
) {
    /**
     * Maps hourly weather data to its response.
     *
     * @param weather the hourly weather data
     */
    public ForecastHourResponse(final ForecastHourWeatherDTO weather) {
        this(
            weather.date(),
            weather.dateEpoch(),
            weather.code(),
            weather.temperatureC(),
            weather.windKMH(),
            weather.windDegree(),
            weather.humidity(),
            weather.cloud(),
            weather.feelsLikeC(),
            weather.uv()
        );
    }
}
