package jpolanco.springbootapp.shared.infrastructure.errors;

import jpolanco.springbootapp.shared.domain.utils.DomainError;
import jpolanco.springbootapp.shared.domain.utils.Error;
import lombok.Getter;

import java.util.List;


@Getter
public class BusinessRuleException extends RuntimeException {
    private final List<Error> errors;

    public BusinessRuleException(List<Error> errors) {
        this.errors = errors != null ? errors : List.of();
    }

    public BusinessRuleException(Error error) {
        this.errors = List.of(error);
    }

    @Override
    public String getMessage() {
        return errors.stream()
                .map(error -> String.format("Field: %s, Message: %s, Code: %s, Details: %s",
                        error.getField(), error.getMessage(), error.getCode(),
                        (error instanceof DomainError e) ? e.getDetails() : "N/A"))
                .reduce((first, second) -> first + "\n" + second)
                .orElse("No errors found");
    }
}
