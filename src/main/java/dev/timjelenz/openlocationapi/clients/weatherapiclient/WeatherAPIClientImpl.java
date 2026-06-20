package dev.timjelenz.openlocationapi.clients.weatherapiclient;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.timjelenz.openlocationapi.clients.weatherapiclient.dto.ForecastDayWeatherDTO;
import dev.timjelenz.openlocationapi.clients.weatherapiclient.dto.ForecastHourWeatherDTO;
import dev.timjelenz.openlocationapi.clients.weatherapiclient.dto.WeatherLocationDTO;
import dev.timjelenz.openlocationapi.clients.weatherapiclient.dto.WeatherResponseDTO;
import dev.timjelenz.openlocationapi.dto.requests.Coordinates;
import dev.timjelenz.openlocationapi.dto.requests.DurationRequest;
import dev.timjelenz.openlocationapi.exceptions.clients.APIResourceNotFoundException;
import dev.timjelenz.openlocationapi.exceptions.clients.ServerSideException;
import dev.timjelenz.openlocationapi.models.Location;
import dev.timjelenz.openlocationapi.services.LocationService;

@Component
public class WeatherAPIClientImpl implements WeatherAPIClient {

    private final LocationService locationService;
    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private final String apiKey;

    public WeatherAPIClientImpl(
        final LocationService locationService,
        final RestClient restClient,
        final ObjectMapper objectMapper,
        @Value("${weather.api.key}")
        final String apiKey
    ) {
        this.locationService = locationService;
        this.restClient = restClient;
        this.objectMapper = objectMapper;
        this.apiKey = apiKey;
    }

    /**
     * Gets the current weather by location id.
     * 
     * @param coordinates coordinates exactly specifying the location
     * @return a weather response DTO containing ForecastHourWeatherDTO
     */
    @Override
    public WeatherResponseDTO<ForecastHourWeatherDTO> getCurrentWeather(final Coordinates coordinates) {
        Location location = locationService.getExactLocation(coordinates);
        
        final HashMap<String, String> paramMap = new HashMap<>();

        paramMap.put("key", apiKey);
        paramMap.put("q", location.getLocationName());
        paramMap.put("aqi", "no");

        JsonNode root = retrieveGetRequest(paramMap, "forecast/json/")
            .body(JsonNode.class);

        if (root == null) {
            throw new ServerSideException("Recieved empty Json.");
        }

        final ForecastHourWeatherDTO forecastHourWeatherDTO;
        final WeatherLocationDTO weatherLocationDTO;

        try {

            forecastHourWeatherDTO = objectMapper.treeToValue(
                root.path("current"), ForecastHourWeatherDTO.class
            );

            weatherLocationDTO = objectMapper.treeToValue(
                root, WeatherLocationDTO.class
            );

        } catch (JsonProcessingException e) {
            throw new ServerSideException("Recieved Json couldn't be decoded.");
        }

        return new WeatherResponseDTO<>(
            weatherLocationDTO,
            forecastHourWeatherDTO
        );
    }

    /**
     * Gets forecast weather.
     * 
     * @param coordinates coordinates exactly specifying the location
     * @param duration timespan specifying from which date the weather gets pulled,
     * rounded down to days
     * @return a weather response DTO, containing a list of forecastdayweather
     */
    @Override
    public WeatherResponseDTO<List<ForecastDayWeatherDTO>> getForecastWeather(
        final Coordinates coordinates,
        final DurationRequest duration
    ) {
        final Location location = locationService.getExactLocation(coordinates);
        final int requestedDays = durationToDays(duration);
        
        final HashMap<String, String> paramMap = new HashMap<>();
        
        paramMap.put("key", apiKey);
        paramMap.put("q", location.getLocationName());
        paramMap.put("days", "%d".formatted(requestedDays));
        paramMap.put("aqi", "no");
        paramMap.put("alerts", "no");

        final JsonNode root = retrieveGetRequest(paramMap, "forecast.json").body(JsonNode.class);

        if (root == null) {
            throw new ServerSideException("Recieved empty Json.");
        }

        final List<ForecastDayWeatherDTO> forecastDayWeatherDTOs;
        final WeatherLocationDTO weatherLocationDTO;

        try {

            forecastDayWeatherDTOs = objectMapper.treeToValue(
                root.path("forecast").path("forecastday"),
                new TypeReference<List<ForecastDayWeatherDTO>>() { }
            );

            weatherLocationDTO = objectMapper.treeToValue(
                root.path("location"), WeatherLocationDTO.class
            );

        } catch (JsonProcessingException e) {
            throw new ServerSideException("Recieved Json couldn't be decoded.");
        }

        return new WeatherResponseDTO<List<ForecastDayWeatherDTO>>(
            weatherLocationDTO,
            forecastDayWeatherDTOs
        );
    }

    /**
     * Utility method that retrieves a get request.
     * 
     * @param paramMap a map of String containing the query - Parameters
     * @param subPath the path that gets added to the restclient base path. For Example
     * subPath = "foo/" will be converted to {basePath}/foo
     * @return a response spec
     */
    private RestClient.ResponseSpec retrieveGetRequest(final Map<String, String> paramMap, final String subPath) {

        final MultiValueMap<String, String> multiValueMap = MultiValueMap.fromSingleValue(paramMap);

        return restClient.get()
            .uri(
                (uriBuilder) -> uriBuilder.path(subPath)
                    .queryParams(multiValueMap)
                    .build()
            )
            .retrieve()
            .onStatus(
                status -> status.is4xxClientError(),
                (req, res) -> {throw new APIResourceNotFoundException();}
            )
            .onStatus(
                status -> status.is5xxServerError(),
                (req, res) -> {throw new ServerSideException("Internal Server Error.");}
            );
    }
    
    private static short durationToDays(DurationRequest duration) {
        return (short) Duration.between(
            duration.from(), duration.to()
        ).toDays();
    }
}
