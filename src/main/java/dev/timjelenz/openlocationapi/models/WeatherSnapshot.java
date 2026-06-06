package dev.timjelenz.openlocationapi.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.Nationalized;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

/**
 * Entity for `WeatherSnapshots` table.
 */
@Getter
@Entity
@Table(
    name = "WeatherSnapshots",
    uniqueConstraints = @UniqueConstraint(
        columnNames  = {"LocationId", "SnapshotTime"}
    )
)
public class WeatherSnapshot {
    /**
     * Protected constructor for Hibernate.
     */
    protected WeatherSnapshot() { }

    /**
     * Constructor to initalize the columns directly.
     *
     * @param location location of the captured weather in the snapshot
     * @param snapshotTime time when the snapshot was taken
     * @param temperatureC temperature (Celcius)
     * @param windKMH wind speed (KMH)
     * @param humidity humidity
     * @param uv uv index
     * @param sourceLastUpdated when the weather information got updated the
     * last time in the API
     * @param apiConditionalCode conditional code from the API
     * @param rawJSON raw json of the weather API respone
     */
    public WeatherSnapshot(
            final Location location,
            final LocalDateTime snapshotTime,
            final BigDecimal temperatureC,
            final BigDecimal windKMH,
            final short humidity,
            final short uv,
            final LocalDateTime sourceLastUpdated,
            final short apiConditionalCode,
            final String rawJSON
    ) {
        this.location = location;
        this.snapshotTime = snapshotTime;
        this.temperatureC = temperatureC;
        this.windKMH = windKMH;
        this.humidity = humidity;
        this.uv = uv;
        this.sourceLastUpdated = sourceLastUpdated;
        this.apiConditionalCode = apiConditionalCode;
        this.rawJSON = rawJSON;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Setter
    @ManyToOne
    @JoinColumn(name = "LocationId", nullable = false)
    private Location location;

    @Setter
    @Column(name = "SnapshotTime", nullable = false)
    private LocalDateTime snapshotTime;

    @Setter
    @Column(nullable = false)
    private LocalDateTime localTime;

    @Setter
    @Column(precision = 4, scale = 1, nullable = false)
    private BigDecimal temperatureC;

    @Setter
    @Column(precision = 5, scale = 1, nullable = false)
    private BigDecimal windKMH;

    @Setter
    @Column(nullable = false)
    @Min(0)
    @Max(100)
    private short humidity;

    @Setter
    @Column(nullable = false)
    @Min(0)
    @Max(24)
    private short uv;

    @Setter
    @Column(nullable = false)
    private LocalDateTime sourceLastUpdated;

    @Setter
    @Column(nullable = false)
    private short apiConditionalCode;

    @Setter
    @Lob
    @Nationalized
    @Column(nullable = false)
    private String rawJSON;

    /**
     * Sets the snapshotTime before the insert
     * into the DB.
     */
    @PrePersist
    public void setSnapShotTime() {
        if (snapshotTime == null) {
            snapshotTime = LocalDateTime.now();
        }
    }
}
