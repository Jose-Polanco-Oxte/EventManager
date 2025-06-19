package jpolanco.springbootapp.user.infrastructure.components.utils;

public enum UserSortField {
    NAME("firstName"),
    EMAIL("email"),
    CREATION("createdAt"),
    NONE("none");

    private final String field;

    UserSortField(String field) {
        this.field = field;
    }

    public String getField() {
        return field;
    }
}
