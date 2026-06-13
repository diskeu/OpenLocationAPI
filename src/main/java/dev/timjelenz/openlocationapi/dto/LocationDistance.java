package dev.timjelenz.openlocationapi.dto;

import java.math.BigDecimal;

import dev.timjelenz.openlocationapi.models.Location;

/**
 * LocationDistance DTO.
 */
public record LocationDistance(
    Location location,
    BigDecimal distance
) { }
