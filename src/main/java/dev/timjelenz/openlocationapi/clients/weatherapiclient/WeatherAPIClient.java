package dev.timjelenz.openlocationapi.clients.weatherapiclient;

import java.util.List;

import dev.timjelenz.openlocationapi.clients.weatherapiclient.dto.ForecastDayWeatherDTO;
import dev.timjelenz.openlocationapi.clients.weatherapiclient.dto.ForecastHourWeatherDTO;
import dev.timjelenz.openlocationapi.clients.weatherapiclient.dto.WeatherResponseDTO;
import dev.timjelenz.openlocationapi.dto.requests.Coordinates;
import dev.timjelenz.openlocationapi.dto.requests.DurationRequest;

/**
 * Client to interact with an external weather api.
 */
public interface WeatherAPIClient {

    WeatherResponseDTO<ForecastHourWeatherDTO> getCurrentWeather(
        Coordinates location
    );

    WeatherResponseDTO<List<ForecastDayWeatherDTO>> getForecastWeather(
        Coordinates location, DurationRequest duration
    );

}
