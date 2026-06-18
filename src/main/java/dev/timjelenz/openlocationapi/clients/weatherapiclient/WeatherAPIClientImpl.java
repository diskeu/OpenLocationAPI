package dev.timjelenz.openlocationapi.clients.weatherapiclient;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.data.domain.PageRequest;
import org.springframework.web.client.RestClient;
import org.springframework.stereotype.Component;

import dev.timjelenz.openlocationapi.clients.ForecastHourWeatherDTO;
import dev.timjelenz.openlocationapi.clients.weatherapiclient.dto.WeatherResponseDTO;
import dev.timjelenz.openlocationapi.dto.LocationDistance;
import dev.timjelenz.openlocationapi.dto.requests.DurationRequest;
import dev.timjelenz.openlocationapi.models.Location;
import dev.timjelenz.openlocationapi.services.LocationService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.MultiValueMap;

import dev.timjelenz.openlocationapi.exceptions.clients.APIResourceNotFoundException;
import dev.timjelenz.openlocationapi.exceptions.clients.ServerSideException;

@Component
public class WeatherAPIClientImpl implements WeatherAPIClient {

    private final LocationService locationService;
    private final RestClient restClient;

    private final String apiKey;

    public WeatherAPIClientImpl(
        final LocationService locationService,
        final RestClient restClient,
        @Value("${weather.api.key}")
        final String apiKey
    ) {
        this.locationService = locationService;
        this.restClient = restClient;
        this.apiKey = apiKey;
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
