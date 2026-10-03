package dev.timjelenz.openlocationapi.dto.responses;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import dev.timjelenz.openlocationapi.clients.weatherapiclient.dto.ForecastDayWeatherDTO;
import dev.timjelenz.openlocationapi.clients.weatherapiclient.dto.ForecastHourWeatherDTO;
import dev.timjelenz.openlocationapi.dto.responses.snapshot.WeatherSnapshotResponse;
import dev.timjelenz.openlocationapi.dto.responses.weather.ForecastDayResponse;
import dev.timjelenz.openlocationapi.models.Location;
import dev.timjelenz.openlocationapi.models.WeatherSnapshot;

class ResponseDtoMappingTest {

    private static final Location LOCATION = createLocation();

    @Test
    void mapsLocation() {
        final LocationResponse response = new LocationResponse(LOCATION);

        assertEquals("Berlin", response.name());
        assertEquals("Europe/Berlin", response.ianaName());
        assertEquals(new BigDecimal("52.520"), response.latitude());
        assertEquals(new BigDecimal("13.405"), response.longitude());
    }

    @Test
    void mapsWeatherSnapshot() {
        final LocalDateTime localTime = LocalDateTime.of(2024, 1, 1, 13, 0);
        final LocalDateTime sourceLastUpdated =
            LocalDateTime.of(2024, 1, 1, 12, 55);

        final WeatherSnapshotResponse response = new WeatherSnapshotResponse(
            new WeatherSnapshot(
                LOCATION,
                LocalDateTime.of(2024, 1, 1, 12, 0),
                localTime,
                new BigDecimal("5.3"),
                new BigDecimal("12.6"),
                (short) 80,
                (short) 1,
                sourceLastUpdated,
                (short) 1006
            )
        );

        assertEquals("Berlin", response.location().name());
        assertEquals(localTime, response.localTime());
        assertEquals(sourceLastUpdated, response.sourceLastUpdated());
        assertEquals(new BigDecimal("5.3"), response.temperatureC());
    }

    @Test
    void mapsForecastDayIncludingHours() {
        final ForecastDayWeatherDTO day = new ForecastDayWeatherDTO(
            List.of(hour()),
            LocalDateTime.of(2024, 1, 1, 0, 0),
            1_704_067_200L,
            1006,
            new BigDecimal("7.2"),
            new BigDecimal("2.1"),
            new BigDecimal("4.6"),
            new BigDecimal("22.0"),
            78,
            1
        );

        final ForecastDayResponse response = new ForecastDayResponse(day);

        assertEquals(new BigDecimal("7.2"), response.maxTempC());
        assertEquals(78, response.avgHumidity());
        assertEquals(1, response.hours().size());
        assertEquals(hour().date(), response.hours().get(0).date());
    }

    @Test
    void mapsSlice() {
        final SliceResponse<LocationResponse> response = SliceResponse.from(
            new PageImpl<>(
                List.of(new LocationResponse(LOCATION)),
                PageRequest.of(0, 10),
                1
            )
        );

        assertEquals(1, response.content().size());
        assertEquals(0, response.page());
        assertEquals(10, response.size());
        assertFalse(response.hasNext());
    }

    private static ForecastHourWeatherDTO hour() {
        return new ForecastHourWeatherDTO(
            LocalDateTime.of(2024, 1, 1, 13, 0),
            1_704_114_000L,
            (short) 1006,
            new BigDecimal("5.3"),
            new BigDecimal("12.6"),
            new BigDecimal("210"),
            (short) 80,
            (short) 75,
            new BigDecimal("3.1"),
            (short) 1
        );
    }

    private static Location createLocation() {
        final Location location = new Location(
            "Berlin",
            "Europe/Berlin"
        );
        location.setLatitude(new BigDecimal("52.520"));
        location.setLongitude(new BigDecimal("13.405"));

        return location;
    }
}
