package dev.timjelenz.openlocationapi.dto.requests;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

class CoordinatesTest {

    @Test
    void acceptsBoundaryValues() {
        assertDoesNotThrow(
            () -> new Coordinates(
                BigDecimal.valueOf(90),
                BigDecimal.valueOf(180)
            )
        );
        assertDoesNotThrow(
            () -> new Coordinates(
                BigDecimal.valueOf(-90),
                BigDecimal.valueOf(-180)
            )
        );
    }

    @Test
    void rejectsOutOfRangeLatitude() {
        assertThrows(
            IllegalArgumentException.class,
            () -> new Coordinates(new BigDecimal("90.1"), BigDecimal.ZERO)
        );
        assertThrows(
            IllegalArgumentException.class,
            () -> new Coordinates(new BigDecimal("-90.1"), BigDecimal.ZERO)
        );
    }

    @Test
    void rejectsOutOfRangeLongitude() {
        assertThrows(
            IllegalArgumentException.class,
            () -> new Coordinates(BigDecimal.ZERO, new BigDecimal("180.1"))
        );
        assertThrows(
            IllegalArgumentException.class,
            () -> new Coordinates(BigDecimal.ZERO, new BigDecimal("-180.1"))
        );
    }

    @Test
    void rejectsNullCoordinates() {
        assertThrows(
            IllegalArgumentException.class,
            () -> new Coordinates(null, BigDecimal.ZERO)
        );
        assertThrows(
            IllegalArgumentException.class,
            () -> new Coordinates(BigDecimal.ZERO, null)
        );
    }

    @Test
    void reportsLongitudeInErrorMessage() {
        final IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Coordinates(BigDecimal.ZERO, new BigDecimal("181"))
        );

        assertEquals(
            "Longitude must be between -180 and 180.",
            exception.getMessage()
        );
    }
}
