package dev.timjelenz.openlocationapi.models;

import dev.timjelenz.openlocationapi.models.embeddables.UserIdLocationId;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Entity for UserStarredLocation table.
 */
@Getter
@Setter
@Entity
@Table(name = "UserStarredLocations")
public class UserStarredLocation {
    /**
     * Protected constructor for Hibernate.
     */
    protected UserStarredLocation() { }

    /**
     * Constructor to initalize the columns directly.
     */
    public UserStarredLocation(User user, Location location) {
        this.user = user;
        this.location = location;
    }

    @EmbeddedId
    private UserIdLocationId userIdLocationId;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "UserId", nullable = false)
    private User user;

    @ManyToOne()
    @MapsId("locationId")
    @JoinColumn(name = "LocationId", nullable = false)
    private Location location;
}
