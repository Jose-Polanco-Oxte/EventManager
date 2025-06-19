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
}
