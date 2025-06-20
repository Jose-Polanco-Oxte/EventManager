package jpolanco.springbootapp.config.errors;

import jpolanco.springbootapp.shared.infrastructure.errors.ProviderException;

public class SecurityAuth extends ProviderException {
  public SecurityAuth(String message, int code, Throwable cause) {
    super(message, code, cause);
  }

  public SecurityAuth(String message, int code) {
    super(message, code);
  }
}
