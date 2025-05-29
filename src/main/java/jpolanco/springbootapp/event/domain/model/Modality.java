package jpolanco.springbootapp.event.domain.model;

public enum Modality {
    IN_PERSON,
    VIRTUAL,
    HYBRID;

    public static Modality create(String modality) {
        return switch (modality.toUpperCase()) {
            case "IN_PERSON" -> IN_PERSON;
            case "VIRTUAL" -> VIRTUAL;
            case "HYBRID" -> HYBRID;
            default -> throw new IllegalArgumentException("Invalid modality: " + modality);
        };
    }
}
