package dev.timjelenz.openlocationapi.exceptions.service.weathersnapshot;

public class WeatherSnapshotNotFoundException extends RuntimeException {

    public WeatherSnapshotNotFoundException() { }

    public WeatherSnapshotNotFoundException(final String msg) {
        super(msg);
    }
}
