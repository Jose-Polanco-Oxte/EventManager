package jpolanco.applicationcore.shared.utils;

/**
 * Custom exception class to handle general exceptions in the application.
 * It extends RuntimeException and provides constructors to set the exception message and cause.
 * If the message is null, it defaults to an empty string.
 * The getCause method is overridden to ensure the cause is returned correctly.
 */
public class GeneralException extends RuntimeException {
    private final Throwable cause;

    public GeneralException(String message, Throwable cause) {
        super(message != null ? message : "", cause);
        this.cause = cause;
    }

    public GeneralException(String message) {
        this(message != null ? message : "", null);
    }

    @Override
    public Throwable getCause() {
        if (cause == null) {
            return super.getCause();
        }
        return cause;
    }
}
