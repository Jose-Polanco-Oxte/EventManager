package jpolanco.springbootapp.event.application.utils;

import java.util.Objects;

public record Changes(
        String field,
        Object before,
        Object after
) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Changes(String field1, String before1, String after1))) return false;
        return Objects.equals(field, field1) &&
                Objects.equals(before, before1) &&
                Objects.equals(after, after1);
    }

    @Override
    public int hashCode() {
        return Objects.hash(field, before, after);
    }
}
