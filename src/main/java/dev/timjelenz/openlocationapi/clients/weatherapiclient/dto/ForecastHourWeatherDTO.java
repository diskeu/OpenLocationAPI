package dev.timjelenz.openlocationapi.clients.weatherapiclient.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import dev.timjelenz.openlocationapi.clients.weatherapiclient.deserializer.ForecastHourDeserializer;

@JsonDeserialize(using = ForecastHourDeserializer.class)
public record ForecastHourWeatherDTO(
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
) {}
