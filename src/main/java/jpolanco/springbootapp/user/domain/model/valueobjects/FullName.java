package jpolanco.springbootapp.user.domain.model.valueobjects;

import jpolanco.springbootapp.shared.utils.results.Report;
import jpolanco.springbootapp.shared.utils.results.Result;
import jpolanco.springbootapp.shared.domain.utils.DomainError;
import jpolanco.springbootapp.shared.utils.results.SuperResult;
import jpolanco.springbootapp.shared.utils.Validators;

import java.util.Objects;
import java.util.Optional;

public class FullName {
    private final String firstName;
    private final String lastName;

    private FullName(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public static SuperResult<FullName, Report> create(String firstName, String lastName) {
        Report report = Report.empty();
        var firstNameFormated = validate(firstName, "firstName");
        var lastNameFormated = validate(lastName, "lastName");
        if (firstNameFormated.isFailure()) {
            report.addError(firstNameFormated.getError());
        }
        if (lastNameFormated.isFailure()) {
            report.addError(lastNameFormated.getError());
        }
        if (report.hasErrors()) return SuperResult.failure(report);
        return SuperResult.success(new FullName(firstNameFormated.getValue(), lastNameFormated.getValue()));
    }

    protected static FullName loadUnchecked(String firstName, String lastName) {
        return new FullName(firstName, lastName);
    }

    private static Result<String> validate(String value, String fieldName) {
        Optional<DomainError> error = Validators.notBlank(fieldName, value);
        if (error.isPresent()) return Result.failure(error.get());

        var formatValue = format(value);

        error = Validators.minLength(fieldName, formatValue, 2);
        if (error.isPresent()) return Result.failure(error.get());

        error = Validators.maxLength(fieldName, formatValue, 100);
        if (error.isPresent()) return Result.failure(error.get());

        if (!formatValue.replaceAll(" ", "").matches("^\\p{L}+$")) {
            return Result.failure(DomainError.INVALID_FORMAT
                    .withField(fieldName)
                    .withDetails("must contain only letters, failed to match: " + formatValue));
        }
        var split = formatValue.split(" ");
        for (String part : split) {
            error = Validators.minLength(fieldName, part, 2);
            if (error.isPresent()) {
                return Result.failure(error.get()
                        .withDetails("Each part of the name must be at least 2 characters long, found: " + part));
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
