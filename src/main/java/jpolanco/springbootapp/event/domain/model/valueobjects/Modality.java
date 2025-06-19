package jpolanco.springbootapp.event.domain.model.valueobjects;

public enum Modality {
    IN_PERSON("IN_PERSON"),
    VIRTUAL("VIRTUAL"),
    HYBRID("HYBRID");

    private final String value;

    Modality(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Modality fromString(String value) {
        for (Modality modality : Modality.values()) {
            if (modality.value.equalsIgnoreCase(value)) {
                return modality;
            }
        }
        throw new IllegalArgumentException("Unknown modality: " + value);
    }
}
