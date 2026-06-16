package dev.timjelenz.openlocationapi.clients.weatherapiclient.dto;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class WeatherLocationDTO {

    private final String name;
    private final String region;
    private final String country;
    private final String tzId;
    private final LocalDateTime localTime;

    public WeatherLocationDTO(
        final String name,
        final String region,
        final String country,
        final String tzId,
        final LocalDateTime localTime
    ) {
        this.name = name;
        this.region = region;
        this.country = country;
        this.tzId = tzId;
        this.localTime = localTime;
    }
}
