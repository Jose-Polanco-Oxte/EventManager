package jpolanco.springbootapp.event.infrastructure.adapters.input.validations.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jpolanco.springbootapp.event.infrastructure.adapters.input.validations.validators.UUIDValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {UUIDValidator.class})
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUUID {
    String message() default "Invalid UUID format";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
