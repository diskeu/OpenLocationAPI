package dev.timjelenz.openlocationapi.clients.weatherapiclient.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record WeatherLocationDTO(
    String name,
    String region,
    String country,

    @JsonProperty("tz_id")
    String tzId,

    @JsonProperty("localtime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    LocalDateTime localTime
) {}
