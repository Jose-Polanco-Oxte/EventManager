package jpolanco.springbootapp.shared.infrastructure.errors;

public class ProviderException extends RuntimeException {
    private final int code;
    private final Throwable cause;

    public ProviderException(String message, int code, Throwable cause) {
        super(message);
        this.code = code;
        this.cause = cause;
    }

    public ProviderException(String message, int code) {
        this(message, code, null);
    }

    public int getCode() {
        return code;
    }

    @Override
    public Throwable getCause() {
        if (cause == null) {
            return super.getCause();
        }
        return cause;
    }
}
