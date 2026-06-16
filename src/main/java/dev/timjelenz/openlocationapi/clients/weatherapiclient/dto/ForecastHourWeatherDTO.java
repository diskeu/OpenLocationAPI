package dev.timjelenz.openlocationapi.clients.weatherapiclient.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class ForecastHourWeatherDTO {

    private final long lastUpdatedEpoch;
    private final LocalDateTime lastUpdated;
    private final short code;
    private final BigDecimal windKMH;
    private final BigDecimal windDegree;
    private final short humidity;
    private final short cloud;
    private final BigDecimal feelsLikeC;
    private final short uv;

    public ForecastHourWeatherDTO(
        final long lastUpdatedEpoch,
        final LocalDateTime lastUpdated,
        final short code,
        final BigDecimal windKMH,
        final BigDecimal windDegree,
        final short humidity,
        final short cloud,
        final BigDecimal feelsLikeC,
        final short uv
    ) {
        this.lastUpdatedEpoch = lastUpdatedEpoch;
        this.lastUpdated = lastUpdated;
        this.code = code;
        this.windKMH = windKMH;
        this.windDegree = windDegree;
        this.humidity = humidity;
        this.cloud = cloud;
        this.feelsLikeC = feelsLikeC;
        this.uv = uv;
    }
}
