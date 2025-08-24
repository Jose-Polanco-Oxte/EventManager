package jpolanco.applicationcore.audit.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark methods or classes for auditing purposes.
 * The action element specifies the action being audited.
 * This annotation can be applied at both method and class levels.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Audit {
    String action();
}
