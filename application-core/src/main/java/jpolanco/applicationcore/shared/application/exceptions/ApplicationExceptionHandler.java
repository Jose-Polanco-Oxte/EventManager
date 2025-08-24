package jpolanco.applicationcore.shared.application.exceptions;

import jpolanco.applicationcore.shared.domain.utils.abstracterrors.ServiceError;
import jpolanco.applicationcore.shared.utils.GeneralException;
import lombok.Getter;
import lombok.NonNull;

import java.util.List;

@Getter
public class ApplicationExceptionHandler extends GeneralException {
    private final List<ServiceError> errors;

    public ApplicationExceptionHandler(@NonNull List<ServiceError> errors) {
        super(errors.toString());
        this.errors = errors;
    }

    public ApplicationExceptionHandler(@NonNull ServiceError error) {
        super(error.toString());
        this.errors = List.of(error);
    }
}
