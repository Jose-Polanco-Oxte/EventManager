package jpolanco.applicationcore.user.domain.model;

import java.util.Optional;

public enum UserRoles {
    USER("USER"),
    ADMIN("ADMIN"),
    ORGANIZER("ORGANIZER");

    private final String value;

    UserRoles(String value) {
        this.value = value;
    }

    public static Optional<UserRoles> fromString(String value) {
        if (value == null || value.isBlank()) {
            return Optional.empty();
        }
        for (UserRoles role : UserRoles.values()) {
            if (role.value.equalsIgnoreCase(value)) {
                return Optional.of(role);
            }
        }
        return Optional.empty();
    }

    public String getValue() {
        return value;
    }
}
