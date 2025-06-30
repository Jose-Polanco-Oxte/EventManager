package jpolanco.springbootapp.shared.domain;

import jpolanco.springbootapp.shared.application.AppError;
import jpolanco.springbootapp.shared.domain.utils.Error;

import java.util.List;

public class CreationReport extends Report {
    private final boolean created;
    private final AppError appError;

    private CreationReport(List<Error> errors, boolean created, AppError appError) {
        super(errors);
        this.created = created;
        this.appError = appError;
    }

    public static CreationReport created() {
        return new CreationReport(List.of(), true, null);
    }

    public static CreationReport failed(List<Error> errors) {
        return new CreationReport(errors != null ? errors : List.of(), false, null);
    }

    public static CreationReport failed(List<Error> errors, AppError appError) {
        return new CreationReport(errors != null ? errors: List.of(), false, appError);
    }

    public boolean isCreated() {
        return created;
    }

    public AppError getAppError() {
        return appError;
    }
}
