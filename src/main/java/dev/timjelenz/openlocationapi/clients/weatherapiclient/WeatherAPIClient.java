package dev.timjelenz.openlocationapi.clients.weatherapiclient;

import java.time.Duration;

import org.springframework.stereotype.Component;

import dev.timjelenz.openlocationapi.clients.weatherapiclient.dto.ForecastDayWeatherDTO;
import dev.timjelenz.openlocationapi.clients.weatherapiclient.dto.ForecastHourWeatherDTO;


@Component
public interface WeatherAPIClient {

    ForecastHourWeatherDTO getCurrentWeather();

    ForecastDayWeatherDTO getForecastWeather(Duration duration);

}
