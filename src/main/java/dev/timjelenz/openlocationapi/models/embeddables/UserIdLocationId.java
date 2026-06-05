package dev.timjelenz.openlocationapi.models.embeddables;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;

/**
 * Composite Primary key for `UserId` and `LocationId`.
 */
@Embeddable
public class UserIdLocationId implements Serializable {
    private int userId;
    private int locationId;

    /**
     * Equals method checks if the given object represents the
     * same `Composite Primary Key`.
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) { return true; }
        if (o == null || o.getClass() != this.getClass()) { return false; }

        UserIdLocationId that = (UserIdLocationId) o;

        return userId == that.userId && locationId == that.locationId;
    }

    /**
     * Returns a hash consisting of `userId` and `locationId`.
     */
    @Override
    public int hashCode() {
        return Objects.hash(userId, locationId);
    }
}
