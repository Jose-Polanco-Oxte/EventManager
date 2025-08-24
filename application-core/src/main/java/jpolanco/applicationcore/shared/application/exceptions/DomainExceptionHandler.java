package jpolanco.applicationcore.shared.application.exceptions;

import jpolanco.applicationcore.shared.domain.utils.abstracterrors.DomainError;
import jpolanco.applicationcore.shared.utils.GeneralException;
import lombok.NonNull;

import java.util.List;

public class DomainExceptionHandler extends GeneralException {
    private final List<DomainError> errors;

    public DomainExceptionHandler(@NonNull List<DomainError> errors) {
        super(errors.toString());
        this.errors = errors;
    }

    public DomainExceptionHandler(@NonNull DomainError error) {
        super(error.toString());
        this.errors = List.of(error);
    }


    @Override
    public String getMessage() {
        return errors.toString();
    }

    public List<DomainError> getErrors() {
        return errors;
    }
}
