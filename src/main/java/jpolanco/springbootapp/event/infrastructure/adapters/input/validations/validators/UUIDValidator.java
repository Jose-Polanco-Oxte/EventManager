package jpolanco.springbootapp.event.infrastructure.adapters.input.validations.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jpolanco.springbootapp.event.infrastructure.adapters.input.validations.annotations.ValidUUID;

public class UUIDValidator implements ConstraintValidator<ValidUUID, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null || s.isEmpty()) {
            return true; // Allow empty strings, as they may be optional
        }
        try {
            java.util.UUID.fromString(s);
            return true; // Valid UUID format
        } catch (IllegalArgumentException e) {
            return false; // Invalid UUID format
        }
    }
}
