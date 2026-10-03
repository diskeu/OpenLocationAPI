package dev.timjelenz.openlocationapi.dto.responses.snapshot;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import dev.timjelenz.openlocationapi.dto.responses.LocationResponse;
import dev.timjelenz.openlocationapi.models.WeatherSnapshot;

/**
 * Public representation of a persisted weather snapshot.
 */
public record WeatherSnapshotResponse(
    int id,
    LocationResponse location,
    LocalDateTime snapshotTime,
    LocalDateTime localTime,
    BigDecimal temperatureC,
    BigDecimal windKMH,
    short humidity,
    short uv,
    LocalDateTime sourceLastUpdated,
    short apiConditionalCode
) {
    /**
     * Maps a weather snapshot entity to its response.
     *
     * @param snapshot the weather snapshot entity
     */
    public WeatherSnapshotResponse(final WeatherSnapshot snapshot) {
        this(
            snapshot.getId(),
            new LocationResponse(snapshot.getLocation()),
            snapshot.getSnapshotTime(),
            snapshot.getLocalTime(),
            snapshot.getTemperatureC(),
            snapshot.getWindKMH(),
            snapshot.getHumidity(),
            snapshot.getUv(),
            snapshot.getSourceLastUpdated(),
            snapshot.getApiConditionalCode()
        );
    }
}
