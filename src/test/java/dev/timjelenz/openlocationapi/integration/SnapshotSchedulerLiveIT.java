package dev.timjelenz.openlocationapi.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import java.time.Duration;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;

import dev.timjelenz.openlocationapi.models.Location;
import dev.timjelenz.openlocationapi.scheduled.snapshot.SnapshotScheduler;

/**
 * Tests the snapshot scheduler against the real WeatherAPI and SQL Server.
 * Disabled unless RUN_LIVE_WEATHER_TESTS=true is set.
 */
@Tag("live")
@EnabledIfEnvironmentVariable(
    named = "RUN_LIVE_WEATHER_TESTS",
    matches = "true"
)
class SnapshotSchedulerLiveIT extends AbstractApiIntegrationTest {

    @Autowired
    private SnapshotScheduler snapshotScheduler;

    @Test
    void storesSnapshotForKnownLocation() {
        final Location berlin = createLocation(
            "Berlin",
            "Europe/Berlin",
            "52.520",
            "13.405"
        );

        snapshotScheduler.saveSnapshots();

        await().atMost(Duration.ofSeconds(30)).untilAsserted(() ->
            assertThat(weatherSnapshotRepository.findAll())
                .filteredOn(snapshot ->
                    snapshot.getLocation().getId() == berlin.getId()
                )
                .singleElement()
                .satisfies(snapshot -> {
                    assertThat(snapshot.getSnapshotTime()).isNotNull();
                    assertThat(snapshot.getLocalTime()).isNotNull();
                    assertThat(snapshot.getTemperatureC()).isNotNull();
                    assertThat(snapshot.getSourceLastUpdated()).isNotNull();
                })
        );
    }
}
