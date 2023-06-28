package jkimble.weatherapp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CoordCall(Double lat, Double lon, String main, String description, Double Temp, Double feelsLike, Double pressure, String name, String country) { }
