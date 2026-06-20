package dev.timjelenz.openlocationapi.clients.weatherapiclient;

import dev.timjelenz.openlocationapi.clients.weatherapiclient.dto.ForecastDayWeatherDTO;
import dev.timjelenz.openlocationapi.clients.weatherapiclient.dto.ForecastHourWeatherDTO;
import dev.timjelenz.openlocationapi.clients.weatherapiclient.dto.WeatherResponseDTO;
import dev.timjelenz.openlocationapi.dto.requests.Coordinates;
import dev.timjelenz.openlocationapi.dto.requests.DurationRequest;

public interface WeatherAPIClient {

    WeatherResponseDTO<ForecastHourWeatherDTO> getCurrentWeather(
        Coordinates location
    );

    WeatherResponseDTO<ForecastDayWeatherDTO> getForecastWeather(
        Coordinates location, DurationRequest duration
    );

}
