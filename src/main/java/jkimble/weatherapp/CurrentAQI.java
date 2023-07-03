package jkimble.weatherapp;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class CurrentAQI {
    @NotNull
    @NotEmpty(message = "Please enter a city name and state, country or both.")
    private String city;

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
