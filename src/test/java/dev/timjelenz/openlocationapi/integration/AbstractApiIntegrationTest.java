package dev.timjelenz.openlocationapi.integration;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MSSQLServerContainer;
import org.testcontainers.utility.DockerImageName;

import dev.timjelenz.openlocationapi.models.Location;
import dev.timjelenz.openlocationapi.models.User;
import dev.timjelenz.openlocationapi.models.WeatherSnapshot;
import dev.timjelenz.openlocationapi.repositories.LocationRepository;
import dev.timjelenz.openlocationapi.repositories.UserRepository;
import dev.timjelenz.openlocationapi.repositories.WeatherSnapshotRepository;

/**
 * Base class for integration tests that run against a real HTTP server,
 * a real SQL Server database and the real application context.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration")
public abstract class AbstractApiIntegrationTest {

    private static final MSSQLServerContainer<?> SQL_SERVER =
        new MSSQLServerContainer<>(
            DockerImageName.parse(
                "mcr.microsoft.com/mssql/server:2022-latest"
            )
        ).acceptLicense();

    static {
        SQL_SERVER.start();
    }

    @DynamicPropertySource
    static void databaseProperties(final DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", SQL_SERVER::getJdbcUrl);
        registry.add(
            "spring.datasource.username",
            SQL_SERVER::getUsername
        );
        registry.add(
            "spring.datasource.password",
            SQL_SERVER::getPassword
        );
    }

    @Autowired
    protected TestRestTemplate restTemplate;

    @Autowired
    protected LocationRepository locationRepository;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected WeatherSnapshotRepository weatherSnapshotRepository;

    @Autowired
    private DataSource dataSource;

    /**
     * Removes all rows between tests. HTTP requests run in their own
     * threads, so a transaction rollback would not isolate the tests.
     */
    @BeforeEach
    void cleanDatabase() {
        new ResourceDatabasePopulator(
            new ClassPathResource("integration/cleanup.sql")
        ).execute(dataSource);
    }

    protected Location createLocation(
        final String name,
        final String ianaName,
        final String latitude,
        final String longitude
    ) {
        final Location location = new Location(name, ianaName);
        location.setLatitude(new BigDecimal(latitude));
        location.setLongitude(new BigDecimal(longitude));

        return locationRepository.saveAndFlush(location);
    }

    protected User createUser(final String userName, final String email) {
        return userRepository.saveAndFlush(
            new User(userName, email, "$2a$10$integration-test-hash")
        );
    }

    protected WeatherSnapshot createSnapshot(
        final Location location,
        final LocalDateTime snapshotTime,
        final String temperatureC,
        final String windKMH,
        final short humidity,
        final short uv,
        final short apiConditionalCode
    ) {
        return weatherSnapshotRepository.saveAndFlush(
            new WeatherSnapshot(
                location,
                snapshotTime,
                snapshotTime.plusHours(1),
                new BigDecimal(temperatureC),
                new BigDecimal(windKMH),
                humidity,
                uv,
                snapshotTime.minusMinutes(5),
                apiConditionalCode
            )
        );
    }
}
