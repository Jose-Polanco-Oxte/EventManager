package jpolanco.springbootapp.shared.utils.results;

import jpolanco.springbootapp.shared.domain.utils.Error;

import java.util.ArrayList;
import java.util.List;

public class Report {
    private final List<Error> errors = new ArrayList<>();

    public Report(List<Error> errors) {
        if (errors != null) {
            this.errors.addAll(errors);
        }
    }

    public List<Error> getErrors() {
        return errors;
    }

    public static Report empty() {
        return new Report(new ArrayList<>());
    }

    public static Report success() {
        return new Report(List.of());
    }

    public static Report failure(List<Error> errors) {
        return new Report(errors != null ? errors : List.of());
    }

    public static Report failure(Error error) {
        return new Report(error != null ? List.of(error) : List.of());
    }

    public void addError(Error error) {
        if (error != null) {
            errors.add(error);
        }
    }

    public boolean hasErrors() {
        return errors != null && !errors.isEmpty();
    }
}
