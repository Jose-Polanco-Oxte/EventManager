package jpolanco.domainmodel.user;

public enum UserStatusE {
    ACTIVE("ACTIVE"),
    INACTIVE("INACTIVE"),
    SUSPENDED("SUSPENDED");

    private final String value;

    UserStatusE(String value) {
        this.value = value;
    }

    public static UserStatusE fromString(String value) {
        for (UserStatusE status : UserStatusE.values()) {
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
