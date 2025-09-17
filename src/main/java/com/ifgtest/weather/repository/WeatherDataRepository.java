package com.ifgtest.weather.repository;

import com.ifgtest.weather.model.WeatherData;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class WeatherDataRepository implements PanacheRepository<WeatherData>{
    public List<WeatherData> findByLocationId(String locationId) {
        return list("locationId", locationId);
    }

    public List<WeatherData> findUnprocessed() {
        return list("processed", false);
    }
}
