package jpolanco.springbootapp.shared.infrastructure.errors;

import jpolanco.springbootapp.shared.domain.utils.Error;

public class InfrastructureError extends Error {
    public InfrastructureError(int code, String message) {
        super(code, message);
    }

    public InfrastructureError(int code, String message, String field) {
        super(code, message, field);
    }

    public static final InfrastructureError INVALID_HEADER = new InfrastructureError(400, "Invalid header format");
    public static final InfrastructureError EXTERNAL_SERVICE_ERROR = new InfrastructureError(502, "External service error");

    public InfrastructureError withField(String fieldName) {
        return new InfrastructureError(this.getCode(), this.getMessage(), fieldName);
    }

    public InfrastructureError withMessage(String message) {
        return new InfrastructureError(this.getCode(), message, this.getField());
    }

    public InfrastructureError withCode(int code) {
        return new InfrastructureError(code, this.getMessage(), this.getField());
    }
}
