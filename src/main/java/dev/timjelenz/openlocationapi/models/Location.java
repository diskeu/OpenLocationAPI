package dev.timjelenz.openlocationapi.models;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Locations entity.
 */
@Getter
@Entity
@Table(name = "Locations")
public class Location {
    /**
     * Protected constructor for Hibernate.
     */
    protected Location() { }

    /**
     * Constructor to initalize the columns directly.
     *
     * @param locationName name of the location
     * @param ianaName the `IANA` name of the location
     */
    public Location(final String locationName, final String ianaName) {
        this.locationName = locationName;
        this.ianaName = ianaName;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Setter
    @Column(
        length = 255,
        unique = true,
        nullable = false
    )
    private String locationName;

    @Setter
    @Column(
        length = 48,
        unique = true,
        nullable = false
    )
    private String ianaName;

    @OneToMany(mappedBy = "location", cascade = CascadeType.REMOVE)
    private List<UserStarredLocation> userStarredLocations;

    @OneToMany(mappedBy = "location", cascade = CascadeType.REMOVE)
    private List<WeatherSnapshot> weatherSnapshots;
}
