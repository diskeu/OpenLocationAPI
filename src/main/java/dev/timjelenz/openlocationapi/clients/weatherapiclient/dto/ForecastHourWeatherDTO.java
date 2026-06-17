package dev.timjelenz.openlocationapi.clients.weatherapiclient.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ForecastHourWeatherDTO(
    LocalDateTime date,
    long dateEpoch,
    short code,
    BigDecimal windKMH,
    BigDecimal windDegree,
    short humidity,
    short cloud,
    BigDecimal feelsLikeC,
    short uv
) {}
