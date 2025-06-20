package jpolanco.springbootapp.shared.utils;

public enum OrderField {
    ASC("asc"),
    DESC("desc"),
    NONE("none");

    private final String value;

    OrderField(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static OrderField fromString(String value) {
        for (OrderField orderField : OrderField.values()) {
            if (orderField.value.equalsIgnoreCase(value)) {
                return orderField;
            }
        }
        return NONE; // Default to NONE if no match found
    }
}
