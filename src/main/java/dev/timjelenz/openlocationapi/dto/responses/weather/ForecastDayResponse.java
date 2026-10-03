package dev.timjelenz.openlocationapi.dto.responses.weather;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import dev.timjelenz.openlocationapi.clients.weatherapiclient.dto.ForecastDayWeatherDTO;

/**
 * Public representation of the weather of a specific day.
 */
public record ForecastDayResponse(
    LocalDateTime date,
    long dateEpoch,
    int code,
    BigDecimal maxTempC,
    BigDecimal minTempC,
    BigDecimal avgTempC,
    BigDecimal maxWindKMH,
    int avgHumidity,
    int uv,
    List<ForecastHourResponse> hours
) {
    /**
     * Maps daily weather data to its response.
     *
     * @param weather the daily weather data
     */
    public ForecastDayResponse(final ForecastDayWeatherDTO weather) {
        this(
            weather.date(),
            weather.dateEpoch(),
            weather.code(),
            weather.maxTemC(),
            weather.minTempC(),
            weather.avgTempC(),
            weather.maxWindKMH(),
            weather.avgHumidity(),
            weather.uv(),
            weather.forecastHourWeather()
                .stream()
                .map(ForecastHourResponse::new)
                .toList()
        );
    }
}
