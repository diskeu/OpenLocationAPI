package dev.timjelenz.openlocationapi.controllers;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.timjelenz.openlocationapi.dto.filter.WeatherSnapshotSearchFilter;
import dev.timjelenz.openlocationapi.dto.responses.SliceResponse;
import dev.timjelenz.openlocationapi.dto.responses.snapshot.WeatherSnapshotResponse;
import dev.timjelenz.openlocationapi.services.WeatherSnapshotService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

/**
 * REST endpoints for persisted weather snapshots.
 */
@Validated
@RestController
@RequestMapping("/api/snapshots")
public class WeatherSnapshotController {

    private final WeatherSnapshotService weatherSnapshotService;

    public WeatherSnapshotController(
        final WeatherSnapshotService weatherSnapshotService
    ) {
        this.weatherSnapshotService = weatherSnapshotService;
    }

    /**
     * Returns the snapshot with the given id.
     *
     * @param id the snapshot id
     * @return the snapshot
     */
    @GetMapping("/{id}")
    public WeatherSnapshotResponse getSnapshotById(
        @PathVariable("id") final int id
    ) {
        return new WeatherSnapshotResponse(
            weatherSnapshotService.getWeatherSnapshotById(id)
        );
    }

    /**
     * Searches snapshots matching the given filter. Unset filter
     * values are ignored.
     *
     * @return a slice of matching snapshots
     */
    @GetMapping
    public SliceResponse<WeatherSnapshotResponse> searchSnapshots(
        @RequestParam(value = "id", required = false)
        final Integer id,

        @RequestParam(value = "snapshotTimeMin", required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        final LocalDateTime snapshotTimeMin,

        @RequestParam(value = "snapshotTimeMax", required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        final LocalDateTime snapshotTimeMax,

        @RequestParam(value = "localTimeMin", required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        final LocalDateTime localTimeMin,

        @RequestParam(value = "localTimeMax", required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        final LocalDateTime localTimeMax,

        @RequestParam(value = "temperatureCMin", required = false)
        final BigDecimal temperatureCMin,

        @RequestParam(value = "temperatureCMax", required = false)
        final BigDecimal temperatureCMax,

        @RequestParam(value = "windKMHMin", required = false)
        final BigDecimal windKMHMin,

        @RequestParam(value = "windKMHMax", required = false)
        final BigDecimal windKMHMax,

        @RequestParam(value = "humidityMin", required = false)
        final Short humidityMin,

        @RequestParam(value = "humidityMax", required = false)
        final Short humidityMax,

        @RequestParam(value = "uvMin", required = false)
        final Short uvMin,

        @RequestParam(value = "uvMax", required = false)
        final Short uvMax,

        @RequestParam(value = "sourceLastUpdatedMin", required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        final LocalDateTime sourceLastUpdatedMin,

        @RequestParam(value = "sourceLastUpdatedMax", required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        final LocalDateTime sourceLastUpdatedMax,

        @RequestParam(value = "apiConditionalCodeMin", required = false)
        final Short apiConditionalCodeMin,

        @RequestParam(value = "apiConditionalCodeMax", required = false)
        final Short apiConditionalCodeMax,

        @RequestParam(value = "page", defaultValue = "0")
        @Min(0)
        final int page,

        @RequestParam(value = "size", defaultValue = "20")
        @Min(1) @Max(100)
        final int size
    ) {
        final WeatherSnapshotSearchFilter filter =
            new WeatherSnapshotSearchFilter(
                id,
                snapshotTimeMin,
                snapshotTimeMax,
                localTimeMin,
                localTimeMax,
                temperatureCMin,
                temperatureCMax,
                windKMHMin,
                windKMHMax,
                humidityMin,
                humidityMax,
                uvMin,
                uvMax,
                sourceLastUpdatedMin,
                sourceLastUpdatedMax,
                apiConditionalCodeMin,
                apiConditionalCodeMax
            );

        return SliceResponse.from(
            weatherSnapshotService.searchSnapshots(
                filter,
                PageRequest.of(page, size)
            ).map(WeatherSnapshotResponse::new)
        );
    }
}
