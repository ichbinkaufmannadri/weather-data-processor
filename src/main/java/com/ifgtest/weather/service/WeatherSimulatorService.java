package com.ifgtest.weather.service;

import com.ifgtest.weather.model.WeatherData;
import com.ifgtest.weather.model.Location;
import com.ifgtest.weather.repository.LocationRepository;
import com.ifgtest.weather.repository.WeatherDataRepository;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@ApplicationScoped
public class WeatherSimulatorService {

    @Inject
    LocationRepository locationRepository;

    @Inject
    WeatherDataRepository weatherDataRepository;

    @Inject
    @Channel("weather-out")
    Emitter<WeatherData> emitter;

    private final Random random = new Random();

    @Scheduled(every = "30s")
    @Transactional
    public void updateWeatherData() {
        List<Location> locations = locationRepository.listAll();

        for (Location loc : locations) {
            WeatherData wd = new WeatherData();
            wd.setLocationId(loc.getLocationId());
            wd.setTemperature(randomDouble(10, 35));
            wd.setHumidity(randomDouble(40, 90));
            wd.setWindSpeed(randomDouble(1, 20));
            wd.setConditions(randomCondition());
            wd.setTimestamp(LocalDateTime.now());
            wd.setProcessed(true);

            weatherDataRepository.persist(wd);

            emitter.send(wd);
        }

        System.out.println("Weather data updated for " + locations.size() + " locations at " + LocalDateTime.now());
    }

    private double randomDouble(double min, double max) {
        return min + (max - min) * random.nextDouble();
    }

    private String randomCondition() {
        String[] conditions = {"Sunny", "Cloudy", "Rainy", "Windy", "Stormy", "Snowy"};
        return conditions[random.nextInt(conditions.length)];
    }
}
