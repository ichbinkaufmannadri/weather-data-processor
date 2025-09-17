package com.ifgtest.weather.repository;

import com.ifgtest.weather.model.Location;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LocationRepository implements PanacheRepository<Location> {
    public Location findByLocationId(String locationId) {
        return find("locationId", locationId).firstResult();
    }
}
