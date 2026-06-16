package dev.timjelenz.openlocationapi.clients.weatherapiclient.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;

@Getter
public class ForecastDayWeatherDTO {

   // every ForecastDay Object contains a list of forecastHour objects
   private final List<ForecastHourWeatherDTO> forecastHourWeather;

   private final LocalDateTime date;
   private final long dateEpoch;
   private final int code;
   private final BigDecimal maxTemC;
   private final BigDecimal minTempC;
   private final BigDecimal avgTempC;
   private final BigDecimal maxWindKMH;
   private final int avgHumidity;
   private final int uv;

   public ForecastDayWeatherDTO(
      final List<ForecastHourWeatherDTO> forecastHourWeather,

      final LocalDateTime date,
      final long dateEpoch,
      final int code,
      final BigDecimal maxTemC,
      final BigDecimal minTempC,
      final BigDecimal avgTempC,
      final BigDecimal maxWindKMH,
      final int avgHumidity,
      final int uv
   ) {
      this.forecastHourWeather = forecastHourWeather;
      this.date = date;
      this.dateEpoch = dateEpoch;
      this.code = code;
      this.maxTemC = maxTemC;
      this.minTempC = minTempC;
      this.avgTempC = avgTempC;
      this.maxWindKMH = maxWindKMH;
      this.avgHumidity = avgHumidity;
      this.uv = uv;
   }
}
