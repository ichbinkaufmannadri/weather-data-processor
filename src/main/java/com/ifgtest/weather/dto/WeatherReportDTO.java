package com.ifgtest.weather.dto;

import java.time.LocalDateTime;

public class WeatherReportDTO {
    public Long id;
    public String locationId;
    public String locationName;
    public String city;
    public String country;
    public double latitude;
    public double longitude;
    public Double temperature;
    public Double humidity;
    public Double windSpeed;
    public String conditions;
    public LocalDateTime timestamp;
    public boolean processed;
}
