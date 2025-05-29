package jpolanco.springbootapp.event.domain.errors;

import jpolanco.springbootapp.shared.domain.Error;

public class LocationError extends Error {
    public LocationError(String code, String message) {
        super(code, message);
    }

    public static LocationError FORMAT_ERROR = new LocationError("FORMAT_ERROR", "Location format is invalid");
}
