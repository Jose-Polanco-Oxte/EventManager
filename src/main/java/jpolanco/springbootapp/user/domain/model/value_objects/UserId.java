package jpolanco.springbootapp.user.domain.model.value_objects;

import jpolanco.springbootapp.shared.domain.utils.DomainError;
import jpolanco.springbootapp.shared.domain.IdObject;
import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.shared.utils.Validators;

import java.util.Optional;
import java.util.UUID;

public class UserId extends IdObject {

    private UserId(String value) {
        super(value);
    }

    public static Result<UserId> create(String value) {
        Optional<DomainError> error = Validators.notBlank("UserId", value);
        if (error.isPresent()) return Result.failure(error.get());

        try {
            UUID.fromString(value);
        } catch (IllegalArgumentException e) {
            return Result.failure(DomainError.INVALID_PARAMETER
                    .withDetails(e.getMessage())
                    .withField("UserId"));
        }
        return Result.success(new UserId(value));
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof UserId that)) return false;
        return this.getValue().equals(that.getValue());
    }

    @Override
    public int hashCode() {
        return this.getValue().hashCode();
    }

    @Override
    public String toString() {
        return this.getValue();
    }
}
