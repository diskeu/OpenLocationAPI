package dev.timjelenz.openlocationapi.clients.weatherapiclient.deserializer;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import dev.timjelenz.openlocationapi.clients.weatherapiclient.dto.ForecastHourWeatherDTO;


public class ForecastHourDeserializer extends JsonDeserializer<ForecastHourWeatherDTO> {

    @Override
    public ForecastHourWeatherDTO deserialize(final JsonParser p, final DeserializationContext ctx) throws IOException {
        final JsonNode node = p.getCodec().readTree(p);

        return new ForecastHourWeatherDTO(
            LocalDateTime.parse(
                node.has("time")
                ? node.path("time").asText()
                : node.path("last_updated").asText()
            ),
            (
                node.has("time_epoch")
                ? node.path("time_epoch").asLong()
                : node.path("last_updated_epoch").asLong()
            ),
            (short) node.path("condition").path("code").asInt(),
            new BigDecimal(node.path("wind_kph").asText()),
            new BigDecimal(node.path("wind_degree").asText()),
            (short) node.path("humidity").asInt(),
            (short) node.path("cloud").asInt(),
            new BigDecimal(node.path("feelslike_c").asText()),
            (short) node.path("uv").asInt()
        );
    }
}
