package dev.timjelenz.openlocationapi.repositories;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dev.timjelenz.openlocationapi.models.WeatherSnapshot;

public interface WeatherSnapshotRepository
extends JpaRepository<WeatherSnapshot, Integer> {
    /**
     * Search function to filter specific elements.
     * Elements that should not have a filter will be
     * ignored.
     *
     * @return list of `WeatherSnapshot` that match the
     * filter
     */
    @Query("""
        SELECT ws FROM WeatherSnapshot ws
        WHERE
            (:id is NULL OR ws.id = :id) AND
            (:snapshotTimeMin IS NULL OR
            ws.snapshotTime >= :snapshotTimeMin) AND
            (:snapshotTimeMax IS NULL OR
            ws.snapshotTime <= :snapshotTimeMax) AND
            (:localTimeMin IS NULL OR
            ws.localTime >= :localTimeMin) AND
            (:localTimeMax IS NULL OR
            ws.localTime <= :localTimeMax) AND
            (:temperatureCMin IS NULL OR
            ws.temperatureC >= :temperatureCMin) AND
            (:temperatureCMax IS NULL OR
            ws.temperatureC <= :temperatureCMax) AND
            (:windKMHMin IS NULL OR
            ws.windKMH >= :windKMHMin) AND
            (:windKMHMax IS NULL OR
            ws.windKMH <= :windKMHMax) AND
            (:humidityMin IS NULL OR
            ws.humidity >= :humidityMin) AND
            (:humidityMax IS NULL OR
            ws.humidity <= :humidityMax) AND
            (:uvMin IS NULL OR ws.uv >= :uvMin) AND
            (:uvMax IS NULL OR ws.uv <= :uvMax) AND
            (:sourceLastUpdatedMin IS NULL OR
            ws.sourceLastUpdated >= :sourceLastUpdatedMin) AND
            (:sourceLastUpdatedMax IS NULL OR
            ws.sourceLastUpdated <= :sourceLastUpdatedMax) AND
            (:apiConditionalCodeMin IS NULL OR
            ws.apiConditionalCode >= :apiConditionalCodeMin) AND
            (:apiConditionalCodeMax IS NULL OR
            ws.apiConditionalCode <= :apiConditionalCodeMax)
    """)
    Slice<WeatherSnapshot> search(
        @Param("id") Integer id,

        @Param("snapshotTimeMin") LocalDateTime snapshotTimeMin,
        @Param("snapshotTimeMax") LocalDateTime snapshotTimeMax,

        @Param("localTimeMin") LocalDateTime localTimeMin,
        @Param("localTimeMax") LocalDateTime localTimeMax,

        @Param("temperatureCMin") BigDecimal temperatureCMin,
        @Param("temperatureCMax") BigDecimal temperatureCMax,

        @Param("windKMHMin") BigDecimal windKMHMin,
        @Param("windKMHMax") BigDecimal windKMHMax,

        @Param("humidityMin") Short humidityMin,
        @Param("humidityMax") Short humidityMax,

        @Param("uvMin") Short uvMin,
        @Param("uvMax") Short uvMax,

        @Param("sourceLastUpdatedMin") LocalDateTime sourceLastUpdatedMin,
        @Param("sourceLastUpdatedMax") LocalDateTime sourceLastUpdatedMax,

        @Param("apiConditionalCodeMin") Short apiConditionalCodeMin,
        @Param("apiConditionalCodeMax") Short apiConditionalCodeMax,

        Pageable pageable
    );
}
