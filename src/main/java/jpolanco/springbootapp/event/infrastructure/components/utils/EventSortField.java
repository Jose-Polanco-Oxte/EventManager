package jpolanco.springbootapp.event.infrastructure.components.utils;

public enum EventSortField {
    NAME("name"),
    DATE("schedule"),
    CREATION("createdAt"),
    ATTENDEES("attendeesCount"),
    NONE("none");

    private final String field;

    EventSortField(String value) {
        this.field = value;
    }

    public String getValue() {
        return field;
    }

    public static EventSortField fromString(String value) {
        for (EventSortField sortField : values()) {
            if (sortField.field.equalsIgnoreCase(value)) {
                return sortField;
            }
        }
        return NONE; // Default to NONE if no match found
    }
}
