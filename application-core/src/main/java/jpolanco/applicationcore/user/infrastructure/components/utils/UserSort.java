package jpolanco.applicationcore.user.infrastructure.components.utils;

import jpolanco.applicationcore.shared.infrastructure.utils.types.SortValue;

public enum UserSort implements SortValue {
    NAME("firstName"),
    EMAIL("email"),
    CREATION("createdAt"),
    UNSORTED("unsorted");

    private final String field;

    UserSort(String field) {
        this.field = field;
    }

    public static UserSort fromString(String field) {
        for (UserSort sortField : UserSort.values()) {
            if (sortField.field.equalsIgnoreCase(field)) {
                return sortField;
            }
        }
        throw new IllegalArgumentException("No matching UserSortField for field: " + field);
    }

    @Override
    public String getSortValue() {
        return field;
    }

    @Override
    public boolean unsorted() {
        return this == UNSORTED;
    }
}
