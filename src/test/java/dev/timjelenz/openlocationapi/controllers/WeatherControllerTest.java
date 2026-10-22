package dev.timjelenz.openlocationapi.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import dev.timjelenz.openlocationapi.clients.weatherapiclient.dto.ForecastDayWeatherDTO;
import dev.timjelenz.openlocationapi.clients.weatherapiclient.dto.ForecastHourWeatherDTO;
import dev.timjelenz.openlocationapi.clients.weatherapiclient.dto.WeatherLocationDTO;
import dev.timjelenz.openlocationapi.clients.weatherapiclient.dto.WeatherResponseDTO;
import dev.timjelenz.openlocationapi.dto.requests.DurationRequest;
import dev.timjelenz.openlocationapi.exceptions.service.location.LocationNotFoundException;
import dev.timjelenz.openlocationapi.services.WeatherService;

@WebMvcTest(WeatherController.class)
@AutoConfigureMockMvc(addFilters = false)
class WeatherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private WeatherService weatherService;

    private WeatherLocationDTO locationDto() {
        return new WeatherLocationDTO(
            "Berlin", "Berlin", "Germany", "Europe/Berlin", LocalDateTime.now()
        );
    }

    private ForecastHourWeatherDTO hourDto() {
        return new ForecastHourWeatherDTO(
            LocalDateTime.now(), 0L, (short) 0, BigDecimal.ONE, BigDecimal.ONE,
            BigDecimal.ZERO, (short) 50, (short) 0, BigDecimal.ONE, (short) 1
        );
    }

    private ForecastDayWeatherDTO dayDto() {
        return new ForecastDayWeatherDTO(
            List.of(hourDto()), LocalDateTime.now(), 0L, 0, BigDecimal.ONE,
            BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE, 50, 1
        );
    }

    @Test
    void returnsCurrentWeather() throws Exception {
        final WeatherResponseDTO<ForecastHourWeatherDTO> response =
            new WeatherResponseDTO<>(locationDto(), hourDto());
        when(weatherService.getCurrentWeather(1)).thenReturn(response);

        mockMvc.perform(get("/api/locations/1/weather"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.location.name").value("Berlin"))
            .andExpect(jsonPath("$.current.temperatureC").value(1));
    }

    @Test
    void returnsNotFoundForUnknownLocationCurrentWeather() throws Exception {
        when(weatherService.getCurrentWeather(999))
            .thenThrow(new LocationNotFoundException());

        mockMvc.perform(get("/api/locations/999/weather"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.detail").value("Location not found"));
    }

    @Test
    void returnsForecast() throws Exception {
        final WeatherResponseDTO<List<ForecastDayWeatherDTO>> response =
            new WeatherResponseDTO<>(locationDto(), List.of(dayDto()));
        when(weatherService.getForecastWeather(eq(1), any(DurationRequest.class)))
            .thenReturn(response);

        mockMvc.perform(get(
            "/api/locations/1/weather/forecast"
                + "?from=2026-11-03T00:00:00Z&to=2026-11-06T00:00:00Z"
        ))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.location.name").value("Berlin"))
            .andExpect(jsonPath("$.forecast").isArray());
    }

    @Test
    void rejectsForecastWithoutTimeRange() throws Exception {
        mockMvc.perform(get("/api/locations/1/weather/forecast"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void returnsNotFoundForUnknownLocationForecast() throws Exception {
        when(weatherService.getForecastWeather(eq(999), any(DurationRequest.class)))
            .thenThrow(new LocationNotFoundException());

        mockMvc.perform(get(
            "/api/locations/999/weather/forecast"
                + "?from=2026-11-03T00:00:00Z&to=2026-11-06T00:00:00Z"
        ))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.detail").value("Location not found"));
    }
}