package dev.timjelenz.openlocationapi.dto.filter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record WeatherSnapshotSearchFilter(

    Integer id,

    LocalDateTime snapshotTimeMin,
    LocalDateTime snapshotTimeMax,

    LocalDateTime localTimeMin,
    LocalDateTime localTimeMax,

    BigDecimal temperatureCMin,
    BigDecimal temperatureCMax,

    BigDecimal windKMHMin,
    BigDecimal windKMHMax,

    Short humidityMin,
    Short humidityMax,

    Short uvMin,
    Short uvMax,

    LocalDateTime sourceLastUpdatedMin,
    LocalDateTime sourceLastUpdatedMax,

    Short apiConditionalCodeMin,
    Short apiConditionalCodeMax

) {}
