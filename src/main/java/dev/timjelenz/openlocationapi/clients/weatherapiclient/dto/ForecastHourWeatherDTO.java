package dev.timjelenz.openlocationapi.clients.weatherapiclient.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ForecastHourWeatherDTO(
    long lastUpdatedEpoch,
    LocalDateTime lastUpdated,
    short code,
    BigDecimal windKMH,
    BigDecimal windDegree,
    short humidity,
    short cloud,
    BigDecimal feelsLikeC,
    short uv
) {}
