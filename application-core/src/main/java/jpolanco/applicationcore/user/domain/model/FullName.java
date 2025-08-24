package jpolanco.applicationcore.user.domain.model;

import jpolanco.applicationcore.shared.domain.utils.Validators;
import jpolanco.applicationcore.shared.domain.utils.abstracterrors.DomainError;
import jpolanco.applicationcore.shared.domain.utils.primitives.results.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FullName {
    private final String firstName;
    private final String lastName;

    private FullName(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public static Result<FullName, DomainError> create(String firstName, String lastName) {
        Result<String, DomainError> firstNameFormated = validate(firstName, "firstName");
        Result<String, DomainError> lastNameFormated = validate(lastName, "lastName");

        List<DomainError> errors = new ArrayList<>();

        if (firstNameFormated.isFailure()) {
            errors.addAll(firstNameFormated.getErrors());
        }
        if (lastNameFormated.isFailure()) {
            errors.addAll(lastNameFormated.getErrors());
        }
        if (!errors.isEmpty()) {
            return Result.failure(errors);
        }
        return Result.success(new FullName(firstNameFormated.getValue(), lastNameFormated.getValue()));
    }

    protected static FullName loadUnchecked(String firstName, String lastName) {
        return new FullName(firstName, lastName);
    }

    private static Result<String, DomainError> validate(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            return Validators.EMPTY_VALUE(fieldName);
        }

        String formatValue = format(value);

        if (formatValue.length() < 2) {
            return Validators.TOO_SHORT(fieldName, formatValue, 2);
        }

        if (formatValue.length() > 100) {
            return Validators.TOO_LONG(fieldName, formatValue, 100);
        }

        if (!formatValue.replaceAll(" ", "").matches("^\\p{L}+$")) {
            return Validators.INVALID_FORMAT(fieldName, formatValue, "Must contain only letters");
        }

        String[] split = formatValue.split(" ");
        for (String part : split) {
            if (part.length() < 2) {
                return Result.failure(
                        DomainError.validation(fieldName, "Each part of the name must be at least 2 characters long, found: " + part)
                                .withDetails("Part: '" + part + "'"));
            }
        }
        return Result.success(formatValue);
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
