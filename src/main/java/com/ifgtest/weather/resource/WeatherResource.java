package com.ifgtest.weather.resource;

import com.ifgtest.weather.model.WeatherData;
import com.ifgtest.weather.service.WeatherConsumer;
import io.smallrye.mutiny.Multi;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.time.Duration;

@Path("/weather")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class WeatherResource {

    @Inject
    WeatherConsumer weatherConsumer;

    @GET
    @Path("/stream")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public Multi<WeatherData> streamAllWeather() {
        return Multi.createFrom().ticks().every(Duration.ofSeconds(5))
                .flatMap(tick -> Multi.createFrom().iterable(weatherConsumer.getAllWeather()));
    }
}
