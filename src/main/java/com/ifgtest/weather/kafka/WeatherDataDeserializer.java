package com.ifgtest.weather.kafka;

import com.ifgtest.weather.model.WeatherData;
import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class WeatherDataDeserializer extends ObjectMapperDeserializer<WeatherData> {
    public WeatherDataDeserializer() {
        super(WeatherData.class);
    }
}
