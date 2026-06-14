package dev.timjelenz.openlocationapi.models.embeddables;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.Getter;

/**
 * Composite Primary key for `UserId` and `LocationId`.
 */
@Getter
@Builder
@Embeddable
public class UserIdLocationId implements Serializable {
    private final int userId;
    private final int locationId;

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
