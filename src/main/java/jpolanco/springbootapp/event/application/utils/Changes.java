package jpolanco.springbootapp.event.application.utils;

import java.util.Objects;

public record Changes<T>(
        String fieldName,
        T oldValue,
        T newValue
) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Changes<?> other = (Changes<?>) o;
        return fieldName.equals(other.fieldName)
                && oldValue.getClass().equals(other.oldValue.getClass())
                && newValue.getClass().equals(other.newValue.getClass());
    }

    @Override
    public int hashCode() {
        return Objects.hash(fieldName, oldValue.getClass(), newValue.getClass());
    }
}
