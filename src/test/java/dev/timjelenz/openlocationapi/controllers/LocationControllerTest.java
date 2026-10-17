package dev.timjelenz.openlocationapi.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import dev.timjelenz.openlocationapi.exceptions.service.location.LocationNotFoundException;
import dev.timjelenz.openlocationapi.models.Location;
import dev.timjelenz.openlocationapi.services.LocationService;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(LocationController.class)
class LocationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LocationService locationService;

    @Test
    void returnsLocationById() throws Exception {
        when(locationService.getLocationEntityById(1))
            .thenReturn(location());

        mockMvc.perform(get("/api/locations/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Berlin"))
            .andExpect(jsonPath("$.ianaName").value("Europe/Berlin"))
            .andExpect(
                jsonPath("$.latitude").value(52.520)
            );
    }

    @Test
    void returnsNotFoundProblem() throws Exception {
        when(locationService.getLocationEntityById(99))
            .thenThrow(new LocationNotFoundException());

        mockMvc.perform(get("/api/locations/99"))
            .andExpect(status().isNotFound())
            .andExpect(
                content().contentTypeCompatibleWith(
                    MediaType.APPLICATION_PROBLEM_JSON
                )
            )
            .andExpect(jsonPath("$.detail").value("Location not found"));
    }

    @Test
    void rejectsOutOfRangeCoordinates() throws Exception {
        mockMvc.perform(
            get("/api/locations/nearby")
                .param("lat", "91")
                .param("lon", "0")
                .param("radiusKm", "10")
        )
            .andExpect(status().isBadRequest());
    }

    private static Location location() {
        final Location location = new Location(
            "Berlin",
            "Europe/Berlin"
        );
        location.setLatitude(new BigDecimal("52.520"));
        location.setLongitude(new BigDecimal("13.405"));

        return location;
    }
}
