package dev.timjelenz.openlocationapi.services;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import dev.timjelenz.openlocationapi.clients.weatherapiclient.dto.ForecastHourWeatherDTO;
import dev.timjelenz.openlocationapi.clients.weatherapiclient.dto.WeatherLocationDTO;
import dev.timjelenz.openlocationapi.clients.weatherapiclient.dto.WeatherResponseDTO;
import dev.timjelenz.openlocationapi.dto.filter.WeatherSnapshotSearchFilter;
import dev.timjelenz.openlocationapi.exceptions.service.weathersnapshot.WeatherSnapshotNotFoundException;
import dev.timjelenz.openlocationapi.models.Location;
import dev.timjelenz.openlocationapi.models.WeatherSnapshot;
import dev.timjelenz.openlocationapi.repositories.WeatherSnapshotRepository;

@Service
public class WeatherSnapshotService {
    
    private final LocationService locationService;
    private final WeatherSnapshotRepository weatherSnapshotRepository;

    public WeatherSnapshotService(
        final LocationService locationService,
        final WeatherSnapshotRepository weatherSnapshotRepository
    ) {
        this.locationService = locationService;
        this.weatherSnapshotRepository = weatherSnapshotRepository;
    }

    public WeatherSnapshot getWeatherSnapshotById(final int id) {
        return weatherSnapshotRepository.findById(id).orElseThrow(
            WeatherSnapshotNotFoundException::new
        );
    }
    
    /**
     * Safes the given current weather
     *
     * @param weatherResponseDTO the weather to save
     */
    public void saveSnapshot(
        final WeatherResponseDTO<ForecastHourWeatherDTO> weatherResponseDTO
    ) {

        final ForecastHourWeatherDTO data = weatherResponseDTO.data();
        final WeatherLocationDTO weatherLocation = weatherResponseDTO.weatherLocationDTO();
        final Location location = locationService.getLocationEntityByName(
            weatherLocation.name()
        );

        weatherSnapshotRepository.save(
            new WeatherSnapshot(
                location,
                null,
                data.temperatureC(),
                data.windKMH(),
                data.humidity(),
                data.uv(),
                data.date(),
                data.code()
            )
        );
    }

    /**
     * Search function to filter WeatherSnapshots after a
     * specific criteria.
     *
     * @param filter search filter
     * @param pageable 
     * @return list of `WeatherSnapshot` that match the filter 
     */
    public Slice<WeatherSnapshot> searchSnapshots(
        final WeatherSnapshotSearchFilter filter,
        final Pageable pageable
    ) {
        return weatherSnapshotRepository.search(
            filter.id(),
            filter.snapshotTimeMin(),
            filter.snapshotTimeMax(),
            filter.localTimeMin(),
            filter.localTimeMax(),
            filter.temperatureCMin(),
            filter.temperatureCMax(),
            filter.windKMHMin(),
            filter.windKMHMax(),
            filter.humidityMin(),
            filter.humidityMax(),
            filter.uvMin(),
            filter.uvMax(),
            filter.sourceLastUpdatedMin(),
            filter.sourceLastUpdatedMax(),
            filter.apiConditionalCodeMin(),
            filter.apiConditionalCodeMax(),
            pageable
        );
    }
}
