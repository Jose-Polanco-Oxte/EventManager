package jpolanco.springbootapp.event.application.utils;

public record Changes<T>(
        String fieldName,
        T oldValue,
        T newValue
) {
}
