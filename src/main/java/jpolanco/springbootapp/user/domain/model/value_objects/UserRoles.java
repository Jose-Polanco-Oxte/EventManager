package jpolanco.springbootapp.user.domain.model.value_objects;

public enum UserRoles {
    USER("USER"),
    ADMIN("ADMIN"),
    ORGANIZER("ORGANIZER");

    private final String value;

    UserRoles(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static UserRoles fromString(String value) {
        for (UserRoles role : UserRoles.values()) {
            if (role.value.equalsIgnoreCase(value)) {
                return role;
            }
        }
        return null;
    }

    public static boolean isValidRole(String role) {
        return role != null && !role.isBlank() && fromString(role) != null;
    }
}
