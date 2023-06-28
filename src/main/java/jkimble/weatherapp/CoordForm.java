package jkimble.weatherapp;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Digits;

public class CoordForm {

    @NotNull
    @Digits(integer = 2, fraction = 2)
    private Double lat;

    @NotNull
    @Digits(integer = 2, fraction = 2)
    private Double lon;

    public Double getLat() {
        return this.lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return this.lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public String toString() {
        return "CoordForm [lat=" + this.lat + ", lon=" + this.lon + "]";
    }
}
