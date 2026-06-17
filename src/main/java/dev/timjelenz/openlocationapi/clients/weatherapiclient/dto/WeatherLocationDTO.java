package dev.timjelenz.openlocationapi.clients.weatherapiclient.dto;

import java.time.LocalDateTime;

public record WeatherLocationDTO(
    String name,
    String region,
    String country,
    String tzId,
    LocalDateTime localTime
) {}
