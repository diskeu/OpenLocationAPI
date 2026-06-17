package dev.timjelenz.openlocationapi.clients.weatherapiclient.dto;

public record WeatherResponseDTO<T>(
    WeatherLocationDTO weatherLocationDTO,
    T data
) { }
