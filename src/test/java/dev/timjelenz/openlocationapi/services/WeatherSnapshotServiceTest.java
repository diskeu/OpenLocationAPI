package dev.timjelenz.openlocationapi.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.timjelenz.openlocationapi.clients.weatherapiclient.dto.ForecastHourWeatherDTO;
import dev.timjelenz.openlocationapi.clients.weatherapiclient.dto.WeatherLocationDTO;
import dev.timjelenz.openlocationapi.clients.weatherapiclient.dto.WeatherResponseDTO;
import dev.timjelenz.openlocationapi.models.Location;
import dev.timjelenz.openlocationapi.models.WeatherSnapshot;
import dev.timjelenz.openlocationapi.repositories.WeatherSnapshotRepository;

@ExtendWith(MockitoExtension.class)
class WeatherSnapshotServiceTest {

    @Mock
    private LocationService locationService;

    @Mock
    private WeatherSnapshotRepository weatherSnapshotRepository;

    @InjectMocks
    private WeatherSnapshotService weatherSnapshotService;

    @Test
    void storesLocalTimeFromWeatherLocation() {
        final Location location = new Location(
            "Berlin",
            "Europe/Berlin"
        );
        final LocalDateTime localTime = LocalDateTime.of(2024, 1, 1, 13, 0);

        when(locationService.getLocationEntityByName("Berlin"))
            .thenReturn(location);

        final ForecastHourWeatherDTO data = new ForecastHourWeatherDTO(
            LocalDateTime.of(2024, 1, 1, 12, 0),
            1_704_110_400L,
            (short) 1006,
            new BigDecimal("5.3"),
            new BigDecimal("12.6"),
            new BigDecimal("210"),
            (short) 80,
            (short) 75,
            new BigDecimal("3.1"),
            (short) 1
        );
        final WeatherResponseDTO<ForecastHourWeatherDTO> response =
            new WeatherResponseDTO<>(
                new WeatherLocationDTO(
                    "Berlin",
                    "Berlin",
                    "Germany",
                    "Europe/Berlin",
                    localTime
                ),
                data
            );

        weatherSnapshotService.saveSnapshot(response);

        final ArgumentCaptor<WeatherSnapshot> captor =
            ArgumentCaptor.forClass(WeatherSnapshot.class);
        verify(weatherSnapshotRepository).save(captor.capture());

        final WeatherSnapshot saved = captor.getValue();
        assertSame(location, saved.getLocation());
        assertEquals(localTime, saved.getLocalTime());
        assertEquals(data.date(), saved.getSourceLastUpdated());
    }
}
