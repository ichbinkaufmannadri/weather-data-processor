package com.ifgtest.weather.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ifgtest.weather.dto.WeatherReportDTO;
import com.ifgtest.weather.model.WeatherData;
import com.ifgtest.weather.repository.LocationRepository;
import com.ifgtest.weather.repository.WeatherDataRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

import java.time.LocalDateTime;

@ApplicationScoped
public class WeatherDataProcessor {

    private static final Logger LOG = Logger.getLogger(WeatherDataProcessor.class);

    @Inject
    WeatherDataRepository weatherDataRepository;

    @Inject
    LocationRepository locationRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Incoming("weather-data-in")
    @Outgoing("processed-weather-data")
    @Transactional
    public String processWeatherData(String rawData) {
        try {
            LOG.infof("Received raw data: %s", rawData);

            JsonNode node = objectMapper.readTree(rawData);

            String locationId = node.has("locationId") ? node.get("locationId").asText() : null;
            Double temperature = node.has("temperature") ? node.get("temperature").asDouble() : null;
            Double humidity = node.has("humidity") ? node.get("humidity").asDouble() : null;
            Double windSpeed = node.has("windSpeed") ? node.get("windSpeed").asDouble() : null;
            String conditions = node.has("conditions") ? node.get("conditions").asText() : null;
            String unit = node.has("unit") ? node.get("unit").asText() : "celsius";

            if (locationId == null || temperature == null) {
                LOG.warnf("Invalid data received: %s", rawData);
                return null;
            }

            boolean locationExists = locationRepository.findByLocationId(locationId) != null;

            Double temperatureCelsius = temperature;
            if ("fahrenheit".equalsIgnoreCase(unit)) {
                temperatureCelsius = (temperature - 32) * 5 / 9;
            }

            // Save entity to DB
            WeatherData weatherData = new WeatherData(
                    locationId,
                    temperatureCelsius,
                    humidity,
                    windSpeed,
                    conditions,
                    LocalDateTime.now()
            );
            weatherDataRepository.persist(weatherData);

            WeatherReportDTO dto = new WeatherReportDTO();
            dto.id = weatherData.getId();
            dto.locationId = locationId;
            dto.locationName = locationExists ? "Existing Location" : "Unknown";
            dto.city = "";
            dto.country = "";
            dto.latitude = 0.0;
            dto.longitude = 0.0;
            dto.temperature = temperatureCelsius;
            dto.humidity = humidity;
            dto.windSpeed = windSpeed;
            dto.conditions = conditions;
            dto.timestamp = LocalDateTime.now();
            dto.processed = true;

            String processedData = objectMapper.writeValueAsString(dto);

            LOG.infof("Processed data saved & published: %s", processedData);
            return processedData;

        } catch (Exception e) {
            LOG.errorf("Error processing data: %s - %s", rawData, e.getMessage());
            return null;
        }
    }
}
