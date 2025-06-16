package jpolanco.springbootapp.event.infrastructure.components;

public enum EventSortField {
    NAME("name"),
    DATE("schedule"),
    CREATION("createdAt"),
    ATTENDEES("attendeesCount"),
    NONE("none");

    private final String field;

    EventSortField(String field) {
        this.field = field;
    }

    public String getField() {
        return field;
    }
}
