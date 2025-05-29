package jpolanco.springbootapp.event.domain.model.valueobjects;

import jpolanco.springbootapp.event.domain.errors.LocationError;
import jpolanco.springbootapp.shared.domain.Result;

public class Location {
    private double latitude;
    private double longitude;

    private Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public static Result<Location> create(double latitude, double longitude) {
        if (latitude < -90 || latitude > 90) {
            return Result.failure(LocationError.FORMAT_ERROR);
        }

        if (longitude < -180 || longitude > 180) {
            return Result.failure(LocationError.FORMAT_ERROR);
        }

        if (latitude == 0 && longitude == 0) {
            return Result.failure(LocationError.FORMAT_ERROR);
        }

        return Result.success(new Location(latitude, longitude));
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
