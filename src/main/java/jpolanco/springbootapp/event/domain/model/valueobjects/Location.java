package jpolanco.springbootapp.event.domain.model.valueobjects;

import jpolanco.springbootapp.event.domain.errors.EventDomainError;
import jpolanco.springbootapp.shared.domain.Result;

public class Location {
    private String name;
    private String city;
    private String Country;
    private double latitude;
    private double longitude;

    public Location(String name, String city, String country, double latitude, double longitude) {
        this.name = name;
        this.city = city;
        Country = country;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static Result<Location> create(double latitude, double longitude, String name, String city, String country) {
        if (city == null || country == null) {
            return Result.failure(EventDomainError.NULL_VALUE.field("City or Country"));
        }
        if (city.isBlank()
                || country.isBlank()
                || latitude < -90
                || latitude > 90
                || longitude < -180
                || longitude > 180
                || (latitude == 0 && longitude == 0)) {
            return Result.failure(EventDomainError.LOCATION_FORMAT_ERROR);
        }
        return Result.success(new Location(name, city, country, latitude, longitude));
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return Country;
    }
}
