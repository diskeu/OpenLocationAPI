package dev.timjelenz.openlocationapi.scheduled.snapshot;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scheduling.annotation.Scheduled;

import dev.timjelenz.openlocationapi.models.Location;
import dev.timjelenz.openlocationapi.services.LocationService;

@ExtendWith(MockitoExtension.class)
class SnapshotSchedulerTest {

    @Mock
    private RequestSaveSnapshot requestSaveSnapshot;

    @Mock
    private LocationService locationService;

    @Test
    void requestsWeatherForEveryLocationOnEveryRun() {
        final Location berlin = location(
            "Berlin",
            "Europe/Berlin",
            "52.520",
            "13.405"
        );
        final Location potsdam = location(
            "Potsdam",
            "Europe/Prague",
            "52.391",
            "13.064"
        );

        when(locationService.getAllLocations())
            .thenReturn(List.of(berlin), List.of(potsdam));

        final SnapshotScheduler scheduler = new SnapshotScheduler(
            requestSaveSnapshot,
            locationService
        );

        scheduler.saveSnapshots();
        scheduler.saveSnapshots();

        verify(locationService, times(2)).getAllLocations();
        verify(requestSaveSnapshot).requestWeatherAPI(berlin);
        verify(requestSaveSnapshot).requestWeatherAPI(potsdam);
    }

    @Test
    void schedulesSnapshotsDailyAt0044InVienna()
        throws NoSuchMethodException {
        final Scheduled scheduled = SnapshotScheduler.class
            .getMethod("saveSnapshots")
            .getAnnotation(Scheduled.class);

        assertThat(scheduled).isNotNull();
        assertThat(SnapshotScheduler.SNAPSHOT_CRON_EXPR)
            .isEqualTo("0 44 0 * * ?");
        assertThat(SnapshotScheduler.SNAPSHOT_CRON_ZONE)
            .isEqualTo("Europe/Vienna");
        assertThat(scheduled.cron())
            .isEqualTo(SnapshotScheduler.SNAPSHOT_CRON_EXPR);
        assertThat(scheduled.zone())
            .isEqualTo(SnapshotScheduler.SNAPSHOT_CRON_ZONE);
    }

    private Location location(
        final String name,
        final String ianaName,
        final String latitude,
        final String longitude
    ) {
        final Location location = new Location(name, ianaName);
        location.setLatitude(new BigDecimal(latitude));
        location.setLongitude(new BigDecimal(longitude));

        return location;
    }
}
