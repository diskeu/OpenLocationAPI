package dev.timjelenz.openlocationapi.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import dev.timjelenz.openlocationapi.dto.filter.WeatherSnapshotSearchFilter;
import dev.timjelenz.openlocationapi.models.Location;
import dev.timjelenz.openlocationapi.models.WeatherSnapshot;
import dev.timjelenz.openlocationapi.services.WeatherSnapshotService;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(WeatherSnapshotController.class)
class WeatherSnapshotControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private WeatherSnapshotService weatherSnapshotService;

    @Test
    void searchesSnapshotsWithFilterParameters() throws Exception {
        when(weatherSnapshotService.searchSnapshots(any(), any()))
            .thenReturn(
                new PageImpl<>(
                    List.of(snapshot()),
                    PageRequest.of(0, 20),
                    1
                )
            );

        mockMvc.perform(
            get("/api/snapshots")
                .param("id", "1")
                .param("snapshotTimeMin", "2024-01-01T00:00:00")
                .param("humidityMin", "50")
                .param("page", "0")
                .param("size", "20")
        )
            .andExpect(status().isOk())
            .andExpect(
                jsonPath("$.content[0].localTime")
                    .value("2024-01-01T13:00:00")
            )
            .andExpect(jsonPath("$.page").value(0))
            .andExpect(jsonPath("$.hasNext").value(false));

        final ArgumentCaptor<WeatherSnapshotSearchFilter> filterCaptor =
            ArgumentCaptor.forClass(WeatherSnapshotSearchFilter.class);
        final ArgumentCaptor<Pageable> pageableCaptor =
            ArgumentCaptor.forClass(Pageable.class);

        verify(weatherSnapshotService).searchSnapshots(
            filterCaptor.capture(),
            pageableCaptor.capture()
        );

        assertEquals(1, filterCaptor.getValue().id());
        assertEquals(
            Short.valueOf((short) 50),
            filterCaptor.getValue().humidityMin()
        );
        assertEquals(0, pageableCaptor.getValue().getPageNumber());
    }

    @Test
    void rejectsInvalidPageSize() throws Exception {
        mockMvc.perform(
            get("/api/snapshots").param("size", "0")
        )
            .andExpect(status().isBadRequest());
    }

    private static WeatherSnapshot snapshot() {
        final Location location = new Location(
            "Berlin",
            "Europe/Berlin"
        );
        location.setLatitude(new BigDecimal("52.520"));
        location.setLongitude(new BigDecimal("13.405"));

        return new WeatherSnapshot(
            location,
            LocalDateTime.of(2024, 1, 1, 12, 0),
            LocalDateTime.of(2024, 1, 1, 13, 0),
            new BigDecimal("5.3"),
            new BigDecimal("12.6"),
            (short) 80,
            (short) 1,
            LocalDateTime.of(2024, 1, 1, 12, 55),
            (short) 1006
        );
    }
}
