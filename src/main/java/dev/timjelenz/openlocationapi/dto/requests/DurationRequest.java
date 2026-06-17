package dev.timjelenz.openlocationapi.dto.requests;

import java.time.Duration;
import java.time.ZonedDateTime;

import jakarta.validation.constraints.NotNull;

public record DurationRequest (
    @NotNull
    ZonedDateTime from,

    @NotNull
    ZonedDateTime to
) {
    public DurationRequest {
        if (from.isAfter(to)) {
            throw new IllegalArgumentException("`From` can't be after `to`");
        }
        Duration diff = Duration.between(from, to);
        if (diff.toHours() > 7) {
            throw new IllegalArgumentException("Diff between `from` and `to` must be <= 7 days.");
        }
    }
}
