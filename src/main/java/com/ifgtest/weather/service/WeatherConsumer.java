package com.ifgtest.weather.service;

import com.ifgtest.weather.model.WeatherData;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class WeatherConsumer {

    private final ConcurrentHashMap<String, WeatherData> latestWeather = new ConcurrentHashMap<>();

    @Incoming("weather-in")
    public void consume(WeatherData wd) {
        latestWeather.put(wd.getLocationId(), wd);
        System.out.println("Realtime weather update received for location " + wd.getLocationId());
    }

    public WeatherData getLatestWeather(String locationId) {
        return latestWeather.get(locationId);
    }

    public List<WeatherData> getAllWeather() {
        return List.copyOf(latestWeather.values());
    }
}
