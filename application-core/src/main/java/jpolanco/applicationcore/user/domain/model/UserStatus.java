package jpolanco.applicationcore.user.domain.model;

public enum UserStatus {
    ACTIVE("ACTIVE"),
    INACTIVE("INACTIVE"),
    SUSPENDED("SUSPENDED");

    private final String value;

    UserStatus(String value) {
        this.value = value;
    }

    public static UserStatus fromString(String value) {
        for (UserStatus status : UserStatus.values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }
}
