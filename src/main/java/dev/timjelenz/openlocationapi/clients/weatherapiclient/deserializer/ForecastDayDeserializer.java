package dev.timjelenz.openlocationapi.clients.weatherapiclient.deserializer;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import dev.timjelenz.openlocationapi.clients.weatherapiclient.dto.ForecastDayWeatherDTO;
import dev.timjelenz.openlocationapi.clients.weatherapiclient.dto.ForecastHourWeatherDTO;

public class ForecastDayDeserializer extends JsonDeserializer<ForecastDayWeatherDTO>{

    @Override
    public ForecastDayWeatherDTO deserialize(JsonParser jp, DeserializationContext ctx) throws IOException {

        final JsonNode node = jp.getCodec().readTree(jp);
        final JsonNode nodeDay = node.path("day");

        List<ForecastHourWeatherDTO> forecastHours = ctx.readValue(
            node.path("hour")
                .traverse(jp.getCodec()),
            ctx.getTypeFactory()
                .constructCollectionType(
                    List.class,
                    ForecastHourWeatherDTO.class
                )
        );

        return new ForecastDayWeatherDTO(
            forecastHours,
            LocalDateTime.parse(node.path("date").asText()),
            node.path("date_epoch").asLong(),
            nodeDay.path("condition").path("code").asInt(),
            new BigDecimal(nodeDay.path("maxtemp_c").asText()),
            new BigDecimal(nodeDay.path("mintemp_c").asText()),
            new BigDecimal(nodeDay.path("avgtemp_c").asText()),
            new BigDecimal(nodeDay.path("maxwind_kph").asText()),
            nodeDay.path("avg_humidity").asInt(),
            nodeDay.path("uv").asInt()
        );
    }
}
