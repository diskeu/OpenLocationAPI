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
@Setter
@Entity
@Table(
    name = "WeatherSnapshots",
    uniqueConstraints = @UniqueConstraint(
        columnNames  = {"LocationId", "SnapshotTime"}
    )
)
public class WeatherSnapshot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "LocationId", nullable = false)
    private Location location;

    @Column(name = "SnapshotTime", nullable = false)
    private LocalDateTime snapshotTime;

    @Column(nullable = false)
    private LocalDateTime localTime;

    @Column(precision = 4, scale = 1, nullable = false)
    private BigDecimal temperatureC;

    @Column(precision = 5, scale = 1, nullable = false)
    private BigDecimal windKMH;

    @Column(nullable = false)
    @Min(0)
    @Max(100)
    private short humidity;

    @Column(nullable = false)
    @Min(0)
    @Max(24)
    private short uv;

    @Column(nullable = false)
    private LocalDateTime sourceLastUpdated;

    @Column(nullable = false)
    private short apiConditionalCode;

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
