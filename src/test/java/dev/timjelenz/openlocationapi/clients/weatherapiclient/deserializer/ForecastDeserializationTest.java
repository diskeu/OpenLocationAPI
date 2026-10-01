package dev.timjelenz.openlocationapi.clients.weatherapiclient.deserializer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.timjelenz.openlocationapi.clients.weatherapiclient.dto.ForecastDayWeatherDTO;
import dev.timjelenz.openlocationapi.clients.weatherapiclient.dto.ForecastHourWeatherDTO;

class ForecastDeserializationTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void parsesCurrentWeatherTimestamp() throws Exception {
        final ForecastHourWeatherDTO weather = objectMapper.readValue(
            """
            {
              "last_updated": "2024-01-01 13:00",
              "last_updated_epoch": 1704114000,
              "temp_c": 5.3,
              "wind_kph": 12.6,
              "wind_degree": 210,
              "humidity": 80,
              "cloud": 75,
              "feelslike_c": 3.1,
              "uv": 1,
              "condition": { "code": 1006 }
            }
            """,
            ForecastHourWeatherDTO.class
        );

        assertEquals(LocalDateTime.of(2024, 1, 1, 13, 0), weather.date());
    }

    @Test
    void parsesForecastDayDateAndNestedHours() throws Exception {
        final ForecastDayWeatherDTO day = objectMapper.readValue(
            """
            {
              "date": "2024-01-01",
              "date_epoch": 1704067200,
              "day": {
                "maxtemp_c": 7.2,
                "mintemp_c": 2.1,
                "avgtemp_c": 4.6,
                "maxwind_kph": 22.0,
                "avg_humidity": 78,
                "uv": 1,
                "condition": { "code": 1006 }
              },
              "hour": [
                {
                  "time": "2024-01-01 13:00",
                  "time_epoch": 1704114000,
                  "temp_c": 5.3,
                  "wind_kph": 12.6,
                  "wind_degree": 210,
                  "humidity": 80,
                  "cloud": 75,
                  "feelslike_c": 3.1,
                  "uv": 1,
                  "condition": { "code": 1006 }
                }
              ]
            }
            """,
            ForecastDayWeatherDTO.class
        );

        assertEquals(LocalDateTime.of(2024, 1, 1, 0, 0), day.date());
        assertEquals(
            LocalDateTime.of(2024, 1, 1, 13, 0),
            day.forecastHourWeather().get(0).date()
        );
    }
}
