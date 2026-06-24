package dev.timjelenz.openlocationapi.services;

import java.awt.print.Pageable;
import java.util.List;

import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import dev.timjelenz.openlocationapi.clients.ForecastHourWeatherDTO;
import dev.timjelenz.openlocationapi.clients.weatherapiclient.dto.WeatherResponseDTO;
import dev.timjelenz.openlocationapi.dto.filter.WeatherSnapshotSearchFilter;
import dev.timjelenz.openlocationapi.exceptions.service.weathersnapshot.WeatherSnapshotNotFoundException;
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
     * @param weatherResponseDTO the weather to safe
     */
    public void safeSnapshot(final WeatherResponseDTO<ForecastHourWeatherDTO> weatherResponseDTO) {
    }

    /**
     * Search function to filter WeatherSnapshots after a
     * specific criteria.
     *
     * @param filter search filter
     * @param pageable 
     * @return list of `WeatherSnapshot` that match the filter 
     */
    public Slice<WeatherSnapshot> searchSnapshots(WeatherSnapshotSearchFilter filter, Pageable pageable) {
    }
}
