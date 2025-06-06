package jpolanco.springbootapp.user.domain.model.valueobjects;

import jpolanco.springbootapp.shared.domain.Error;
import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.shared.domain.ResultBuilder;
import jpolanco.springbootapp.user.domain.errors.NameError;

public class FullName {
    private final String firstName;
    private final String lastName;

    private FullName(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public static Result<FullName> create(String firstName, String lastName) {
        return ResultBuilder
                .from(ensureValueIsValid(firstName))
                .then(validFirst -> ensureValueIsValid(lastName))
                .then(validLast -> Result.success(new FullName(firstName, validLast)))
                .result();
    }

    private static Result<String> ensureValueIsValid(String value) {
        if (value == null || value.isEmpty()) {
            return Result.failure(Error.NULL_VALUE.field("Names"));
        }
        if (value.length() < 2) {
            return Result.failure(NameError.TOO_SHORT);
        }
        if (value.matches(".*\\d.*")) {
            return Result.failure(NameError.INVALID_CHARACTERS);
        }
        if (value.length() > 50) {
            return Result.failure(NameError.TOO_LONG);
        }
        return Result.success(value);
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
