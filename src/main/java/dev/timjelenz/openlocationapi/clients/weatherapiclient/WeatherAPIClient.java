package dev.timjelenz.openlocationapi.clients.weatherapiclient;

import dev.timjelenz.openlocationapi.clients.weatherapiclient.dto.ForecastDayWeatherDTO;
import dev.timjelenz.openlocationapi.clients.weatherapiclient.dto.ForecastHourWeatherDTO;
import dev.timjelenz.openlocationapi.clients.weatherapiclient.dto.WeatherResponseDTO;
import dev.timjelenz.openlocationapi.dto.requests.DurationRequest;
import dev.timjelenz.openlocationapi.dto.requests.LocationRequest;

public interface WeatherAPIClient {

    WeatherResponseDTO<ForecastHourWeatherDTO> getCurrentWeather(
        LocationRequest location
    );

    WeatherResponseDTO<ForecastDayWeatherDTO> getForecastWeather(
        LocationRequest location, DurationRequest duration
    );

}
