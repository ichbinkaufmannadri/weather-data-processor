package com.ifgtest.weather.health;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@Readiness
@ApplicationScoped
public class KafkaHealthCheck implements HealthCheck {

    @Inject
    @Channel("processed-weather-data")
    Emitter<String> emitter;

    @Override
    public HealthCheckResponse call() {
        try {
            emitter.send("health-check");
            return HealthCheckResponse.up("Kafka connection");
        } catch (Exception e) {
            return HealthCheckResponse.down("Kafka connection");
        }
    }
}
