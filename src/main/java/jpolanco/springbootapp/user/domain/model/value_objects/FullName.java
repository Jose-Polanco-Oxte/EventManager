package jpolanco.springbootapp.user.domain.model.value_objects;

import jpolanco.springbootapp.shared.domain.DomainError;
import jpolanco.springbootapp.shared.domain.Result;

import java.util.Objects;

public class FullName {
    private final String firstName;
    private final String lastName;

    private FullName(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public static Result<FullName> create(String firstName, String lastName) {
        var namesResult = namesValidated(firstName, lastName);
        if (namesResult.isFailure()) {
            return Result.failure(namesResult.getError());
        }
        var first = namesResult.getValue()[0];
        var last = namesResult.getValue()[1];
        return Result.success(new FullName(first, last));
    }

    private static DomainError ensureValueIsValid(String value) {
        if (value == null || value.isEmpty()) {
            return DomainError.NULL_VALUE;
        }
        if (value.length() < 2) {
            return DomainError.TOO_SHORT
                    .withDetails("Must be at least 2 characters long");
        }
        if (value.length() > 100) {
            return DomainError.TOO_LONG
                    .withDetails("Must be at most 100 characters long");
        }
        var split = value.split(" ");
        for (String part : split) {
            if (part.length() < 2) {
                return DomainError.TOO_SHORT
                        .withDetails("Each part of the name must be at least 2 characters long");
            }
            if (!part.matches("^\\p{L}+$")) {
                return DomainError.INVALID_FORMAT
                        .withDetails("Can only contain letters");
            }
        }
        return null;
    }

    private static Result<String[]> namesValidated(String firstName, String lastName) {
        var formatedFirstName = format(firstName);
        var formatedLastName = format(lastName);
        var firstNameResult = ensureValueIsValid(formatedFirstName);
        var lastNameResult = ensureValueIsValid(formatedLastName);
        if (firstNameResult != null) {
            return Result.failure(firstNameResult.getError().withField("firstName"));
        } else if (lastNameResult != null) {
            return Result.failure(lastNameResult.getError().withField("lastName"));
        } else {
            return Result.success(new String[]{
                    formatedFirstName,
                    formatedLastName
            });
        }
    }

    private static String format(String value) {
        if (value == null) {
            return null;
        }
        return value.strip()
                .replaceAll("[\\p{Z}\\p{C}]+", " ")
                .trim();
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FullName other)) return false;
        return firstName.equals(other.firstName)
                && lastName.equals(other.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName);
    }
}
