package dev.timjelenz.openlocationapi.clients.weatherapiclient.dto;

import lombok.Getter;

@Getter
public class WeatherResponseDTO<T> {

    private final WeatherLocationDTO weatherLocationDTO;
    private final T data;
    
    public WeatherResponseDTO (
        final WeatherLocationDTO weatherLocationDTO,
        final T data
    ) {
        this.weatherLocationDTO = weatherLocationDTO;
        this.data = data;
    }

}
