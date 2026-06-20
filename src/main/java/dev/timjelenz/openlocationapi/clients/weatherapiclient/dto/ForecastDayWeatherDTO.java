package dev.timjelenz.openlocationapi.clients.weatherapiclient.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import dev.timjelenz.openlocationapi.clients.weatherapiclient.deserializer.ForecastDayDeserializer;

@JsonDeserialize(using = ForecastDayDeserializer.class)
public record ForecastDayWeatherDTO(
   List<ForecastHourWeatherDTO> forecastHourWeather,

   LocalDateTime date,
   long dateEpoch,
   int code,
   BigDecimal maxTemC,
   BigDecimal minTempC,
   BigDecimal avgTempC,
   BigDecimal maxWindKMH,
   int avgHumidity,
   int uv
) {}
