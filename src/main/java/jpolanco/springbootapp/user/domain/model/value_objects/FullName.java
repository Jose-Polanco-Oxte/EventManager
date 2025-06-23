package jpolanco.springbootapp.user.domain.model.value_objects;

import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.domain.errors.UserDomainError;

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

    private static Result<String> ensureValueIsValid(String value) {
        if (value == null || value.isEmpty()) {
            return Result.failure(UserDomainError.NULL_VALUE.field("Names"));
        }
        var formated = value.strip()
                .replaceAll("[\\p{Z}\\p{C}]+", " ")
                .trim();
        var splited = formated.split(" ");
        for (String part : splited) {
            if (part.length() < 2) {
                return Result.failure(UserDomainError.NAME_TOO_SHORT);
            }
            if (!part.matches("^\\p{L}+$")) {
                return Result.failure(UserDomainError.NAME_INVALID_CHARACTERS);
            }
        }
        if (formated.length() < 2) {
            return Result.failure(UserDomainError.NAME_TOO_SHORT);
        }
        if (formated.length() > 100) {
            return Result.failure(UserDomainError.NAME_TOO_LONG);
        }
        return Result.success(formated);
    }

    private static Result<String[]> namesValidated(String firstName, String lastName) {
        var firstNameResult = ensureValueIsValid(firstName);
        var lastNameResult = ensureValueIsValid(lastName);
        if (firstNameResult.isFailure()) {
            return Result.failure(firstNameResult.getError());
        } else if (lastNameResult.isFailure()) {
            return Result.failure(lastNameResult.getError());
        } else {
            return Result.success(new String[]{firstNameResult.getValue(), lastNameResult.getValue()});
        }
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
}
