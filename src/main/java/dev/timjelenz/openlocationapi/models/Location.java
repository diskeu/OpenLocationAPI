package dev.timjelenz.openlocationapi.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Locations entity.
 */
@Getter
@Setter
@Entity
@Table(name = "Locations")
public class Location {
    @Column(name = "Id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(
        name = "LocationName",
        length = 255,
        unique = true,
        nullable = false
    )
    private String locationName;

    @Column(
        name = "IANAName",
        length = 48,
        unique = true,
        nullable = false
    )
    private String ianaName;
}
