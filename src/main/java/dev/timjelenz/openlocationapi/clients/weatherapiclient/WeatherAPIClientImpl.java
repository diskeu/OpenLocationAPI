package dev.timjelenz.openlocationapi.clients.weatherapiclient;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.client.RestClient;
import org.springframework.stereotype.Component;

import dev.timjelenz.openlocationapi.clients.weatherapiclient.dto.WeatherResponseDTO;
import dev.timjelenz.openlocationapi.dto.requests.DurationRequest;
import dev.timjelenz.openlocationapi.models.Location;
import dev.timjelenz.openlocationapi.services.LocationService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.timjelenz.openlocationapi.clients.weatherapiclient.dto.ForecastHourWeatherDTO;
import dev.timjelenz.openlocationapi.clients.weatherapiclient.dto.WeatherLocationDTO;
import dev.timjelenz.openlocationapi.exceptions.clients.APIResourceNotFoundException;
import dev.timjelenz.openlocationapi.exceptions.clients.ServerSideException;

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
     * @param locationId the unique id specifieing the location
     * @return a weather response DTO containing ForecastHourWeatherDTO
     */
    public WeatherResponseDTO<ForecastHourWeatherDTO> getCurrentWeather(final int locationId) {
        Location location = locationService.getLocationEntityById(locationId);
        
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
