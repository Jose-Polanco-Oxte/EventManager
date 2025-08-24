package jpolanco.applicationcore.shared.domain.utils.abstracterrors;

import java.util.Optional;

public enum DomainErrType implements ErrorTypeV {
    VALIDATION_ERROR,
    BUSINESS_RULE_VIOLATION,
    ;

    @Override
    public Optional<String> get() {
        return Optional.of(name());
    }
}
