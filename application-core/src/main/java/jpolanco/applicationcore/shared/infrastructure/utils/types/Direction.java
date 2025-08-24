package jpolanco.applicationcore.shared.infrastructure.utils.types;

public enum Direction {
    ASC("asc"),
    DESC("desc");

    private final String value;

    Direction(String value) {
        this.value = value;
    }

    public static Direction fromString(String value) {
        for (Direction direction : Direction.values()) {
            if (direction.value.equalsIgnoreCase(value)) {
                return direction;
            }
        }
        throw new IllegalArgumentException("Unknown order field: " + value);
    }

    public String getValue() {
        return value;
    }
}
